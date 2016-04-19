package com.damitchell.tgp.handler.game;

import com.damitchell.tgp.service.GamesService;
import com.damitchell.tgp.service.StatsService;
import com.google.inject.Inject;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.web.RoutingContext;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class WeekHandler extends AbstractGameHandler implements Handler<RoutingContext>
{
    @Inject
    public WeekHandler(RepresentationFactory factory, GamesService gameService, StatsService statsService)
    {
        super(factory, gameService, statsService);
    }

    @Override
    public void handle(RoutingContext context)
    {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);

        handleGame(context, now.minusWeeks(1), now);
    }
}
