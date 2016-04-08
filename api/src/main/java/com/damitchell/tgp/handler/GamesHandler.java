package com.damitchell.tgp.handler;

import com.damitchell.tgp.model.GameModel;
import com.damitchell.tgp.service.GamesService;
import com.google.inject.Inject;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.rxjava.core.MultiMap;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Observable;

import java.util.List;

public class GamesHandler implements Handler<RoutingContext>
{
    private static final Logger logger = LoggerFactory.getLogger(GamesHandler.class);

    private RepresentationFactory factory;
    private GamesService gameService;

    @Inject
    public GamesHandler(RepresentationFactory factory, GamesService gameService)
    {
        this.factory = factory;
        this.gameService = gameService;
    }

    @Override
    public void handle(RoutingContext context)
    {
        HttpServerRequest request = context.request();
        String baseUrl = (request.isSSL() ? "https" : "http") + "://" + request.getHeader(HttpHeaders.HOST.toString());

        Representation rep = factory.newRepresentation(baseUrl + request.uri());

        MultiMap params = request.params();
        Integer limit = params.contains("limit") ? Integer.parseInt(params.get("limit")) : 10;
        Integer offset = params.contains("offset") ? Integer.parseInt(params.get("offset")) : 0;

        Observable<Integer> total = gameService.getTotalGames();
        Observable<List<GameModel>> games = gameService.getGames(limit, offset);

        Observable.zip(total, games, (integer, entries) -> {
            entries.forEach(game -> rep.withRepresentation("games",
                game.getRep(factory.newRepresentation(baseUrl + "/games/" + game.getId()))));

            rep.withProperty("total", integer);

            String template = baseUrl + context.request().uri() + "?limit=%s&offset=%s";

            rep.withLink("next", String.format(template, limit, (offset + limit)));

            if(offset > 0) {
                rep.withLink("prev", String.format(template, limit, ((offset - limit) < 0 ? 0 : (offset - limit))));
            }

            return rep;
        }).subscribe(representation -> {
            context.response()
                .putHeader("content-type", "application/hal+json")
                .end(representation.toString(RepresentationFactory.HAL_JSON));
        }, context::fail);
    }
}
