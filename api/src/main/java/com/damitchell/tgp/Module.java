package com.damitchell.tgp;

import com.damitchell.tgp.handler.GameHandler;
import com.damitchell.tgp.handler.GamesHandler;
import com.damitchell.tgp.handler.TotalHandler;
import com.damitchell.tgp.handler.game.DayHandler;
import com.damitchell.tgp.handler.game.MonthHandler;
import com.damitchell.tgp.handler.game.WeekHandler;
import com.damitchell.tgp.handler.game.WeekHandler;
import com.damitchell.tgp.provider.RoutingProvider;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import com.theoryinpractise.halbuilder.standard.StandardRepresentationFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import io.vertx.rxjava.ext.web.Router;

public class Module extends AbstractModule
{
    private Vertx vertx;

    public Module(Vertx vertx)
    {
        this.vertx = vertx;
    }

    @Override
    protected void configure()
    {
        Config config = ConfigFactory.load();

        bind(Config.class).toInstance(config);
        bind(RepresentationFactory.class).to(StandardRepresentationFactory.class).asEagerSingleton();
        bind(Vertx.class).toInstance(vertx);

        JsonObject dbConfig = new JsonObject()
            .put("url", String.format("jdbc:mysql://%s:%s/%s",
                config.getString("db.host"),
                config.getString("db.port"),
                config.getString("db.name")))
            .put("user", config.getString("db.username"))
            .put("password", config.getString("db.password"));

        bind(JDBCClient.class).toInstance(JDBCClient.createShared(vertx, dbConfig));

        MapBinder<String, Handler<RoutingContext>> handlers = MapBinder.newMapBinder(
            binder(),
            new TypeLiteral<String>() {},
            new TypeLiteral<Handler<RoutingContext>>(){}
        );

        handlers.addBinding("games").to(GamesHandler.class);
        handlers.addBinding("game").to(GameHandler.class);
        handlers.addBinding("game.day").to(DayHandler.class);
        handlers.addBinding("game.week").to(WeekHandler.class);
        handlers.addBinding("game.month").to(MonthHandler.class);
        handlers.addBinding("total").to(TotalHandler.class);
        handlers.addBinding("total.day").to(com.damitchell.tgp.handler.total.DayHandler.class);

        bind(Router.class).toProvider(RoutingProvider.class);
    }
}
