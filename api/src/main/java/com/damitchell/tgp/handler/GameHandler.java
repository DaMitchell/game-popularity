package com.damitchell.tgp.handler;

import com.damitchell.tgp.model.StatModel;
import com.damitchell.tgp.service.GamesService;
import com.damitchell.tgp.service.StatsService;
import com.google.inject.Inject;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.MultiMap;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;
import rx.Observable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHandler implements Handler<RoutingContext>
{
    private RepresentationFactory factory;
    private GamesService gameService;
    private StatsService statsService;

    @Inject
    public GameHandler(RepresentationFactory factory, GamesService gameService, StatsService statsService)
    {
        this.factory = factory;
        this.gameService = gameService;
        this.statsService = statsService;
    }

    @Override
    public void handle(RoutingContext context)
    {
        HttpServerRequest request = context.request();
        MultiMap params = request.params();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate from = params.contains("from") ? LocalDate.parse(params.get("from"), formatter) : LocalDate.now();
        LocalDate to = params.contains("from") && params.contains("to") ?
            LocalDate.parse(params.get("to"), formatter) : from.plusDays(1);

        Integer id = Integer.parseInt(request.getParam("id"));
        Representation rep = factory.newRepresentation(request.absoluteURI());

        rep.withProperty("from", from.format(formatter));
        rep.withProperty("to", from.format(formatter));

        Observable<Representation> gameRep = gameService.getGame(id).map(gameModel -> gameModel.getRep(rep));
        Observable<List<StatModel>> gameStats = statsService.getStatsForGame(id, from, to);

        Observable.zip(gameRep, gameStats, (representation, statsList) -> {
            Map<String, Map<String, Integer>> stats = new HashMap<>();

            statsList.forEach(statModel -> {
                Map<String, Integer> stat = new HashMap<>();
                stat.put("viewers", statModel.getViewers());
                stat.put("channels", statModel.getChannels());

                stats.put(statModel.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), stat);
            });

            return representation.withProperty("stats", stats);
        }).subscribe(representation -> {
            context.response()
                .putHeader("content-type", "application/hal+json")
                .end(representation.toString(RepresentationFactory.HAL_JSON));
        }, context::fail);
    }
}
