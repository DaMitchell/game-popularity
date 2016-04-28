package com.damitchell.tgp.handler.total;

import com.damitchell.tgp.model.GameModel;
import com.damitchell.tgp.model.MostChannelsModel;
import com.damitchell.tgp.model.MostViewersModel;
import com.damitchell.tgp.service.TotalService;
import com.google.inject.Inject;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Observable;

public class DayHandler implements Handler<RoutingContext>
{
    private RepresentationFactory factory;
    private TotalService totalService;

    @Inject
    public DayHandler(RepresentationFactory factory, TotalService totalService)
    {
        this.totalService = totalService;
        this.factory = factory;
    }

    @Override
    public void handle(RoutingContext context)
    {
        HttpServerRequest request = context.request();
        String baseUrl = (request.isSSL() ? "https" : "http") + "://" + request.getHeader(HttpHeaders.HOST.toString());

        Observable<MostViewersModel> mostViewersObservable = totalService.getGameWithMostViewers(TotalService.Type.DAY);
        Observable<MostChannelsModel> mostChannelsObservable = totalService.getGameWithMostChannels(TotalService.Type.DAY);

        Representation rep = factory.newRepresentation(request.absoluteURI());

        Observable.zip(mostViewersObservable, mostChannelsObservable, (mostViewersModel, mostChannelsModel) -> {
            Representation mostViewersRep = mostViewersModel.getRep(factory.newRepresentation());
            GameModel viewersGame = mostViewersModel.getGame();
            mostViewersRep.withRepresentation("game", viewersGame
                .getRep(factory.newRepresentation(baseUrl + "/games/" + viewersGame.getId())));

            rep.withRepresentation("most_viewers", mostViewersRep);

            Representation mostChannelsRep = mostChannelsModel.getRep(factory.newRepresentation());
            GameModel channelsGame = mostChannelsModel.getGame();
            mostChannelsRep.withRepresentation("game", channelsGame
                .getRep(factory.newRepresentation(baseUrl + "/games/" + channelsGame.getId())));

            rep.withRepresentation("most_channels", mostChannelsRep);

            return rep;
        }).subscribe(representation -> {
            context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE.toString(), "application/hal+json")
                .end(representation.toString(RepresentationFactory.HAL_JSON));
        }, context::fail);

    }
}
