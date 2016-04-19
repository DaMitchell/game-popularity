package com.damitchell.tgp.handler.game;

import com.damitchell.tgp.model.StatModel;
import com.damitchell.tgp.service.GamesService;
import com.damitchell.tgp.service.StatsService;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Observable;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbstractGameHandler
{
    private RepresentationFactory factory;
    private GamesService gameService;
    private StatsService statsService;

    public AbstractGameHandler(RepresentationFactory factory, GamesService gameService, StatsService statsService)
    {
        this.factory = factory;
        this.gameService = gameService;
        this.statsService = statsService;
    }

    protected void handleGame(RoutingContext context, ZonedDateTime from, ZonedDateTime to)
    {
        HttpServerRequest request = context.request();

        Integer id = Integer.parseInt(request.getParam("id"));
        Representation rep = factory.newRepresentation(request.absoluteURI());
        rep.withProperty("from", from.format(DateTimeFormatter.ISO_INSTANT));
        rep.withProperty("to", to.format(DateTimeFormatter.ISO_INSTANT));

        Observable<Representation> gameRep = gameService.getGame(id).map(gameModel -> gameModel.getRep(rep));
        Observable<List<StatModel>> gameStats = statsService.getStatsForGame(id, from, to);

        Observable.zip(gameRep, gameStats, (representation, statsList) -> {
            Map<String, Map<String, Integer>> stats = new LinkedHashMap<>();

            statsList.forEach(statModel -> {
                Map<String, Integer> stat = new HashMap<>();
                stat.put("viewers", statModel.getViewers());
                stat.put("channels", statModel.getChannels());

                stats.put(statModel.getDate().format(DateTimeFormatter.ISO_INSTANT/*ofPattern("yyyy-MM-dd HH:mm")*/), stat);
            });

            return representation.withProperty("stats", stats);
        }).subscribe(representation -> {
            context.response()
                .putHeader("content-type", "application/hal+json")
                .end(representation.toString(RepresentationFactory.HAL_JSON));
        }, context::fail);
    }
}
