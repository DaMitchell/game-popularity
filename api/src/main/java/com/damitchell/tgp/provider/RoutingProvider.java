package com.damitchell.tgp.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;
import io.vertx.rxjava.ext.web.handler.CorsHandler;

import java.util.Map;

public class RoutingProvider implements Provider<Router>
{
    private Vertx vertx;
    private Map<String, Handler<RoutingContext>> routes;

    @Inject
    public RoutingProvider(Vertx vertx, Map<String, Handler<RoutingContext>> routes)
    {
        this.vertx = vertx;
        this.routes = routes;
    }

    @Override
    public Router get()
    {
        Router router = Router.router(vertx);

        //TODO replace "tgp.com" with actual address
        router.get().handler(CorsHandler.create("(http[s]?):\\/\\/(localhost:4200|192.168.0.103:4200|(.*\\.)?(tgp.com))")
            .allowedMethod(HttpMethod.GET));

        router.get("/games").handler(routes.get("games"));
        router.get("/games/:id").handler(routes.get("game"));
        router.get("/games/:id/day").handler(routes.get("game.day"));
        router.get("/games/:id/week").handler(routes.get("game.week"));
        router.get("/games/:id/month").handler(routes.get("game.month"));

        router.get("/total").handler(routes.get("total"));
        router.get("/total/day").handler(routes.get("total.day"));
        //router.get("/total/week").handler(routes.get("total.week"));
        //router.get("/total/month").handler(routes.get("total.month"));

        //router.get("/most").handler(routes.get("most"));

        //router.get("/day").handler(routes.get("day"));
        //router.get("/week").handler(routes.get("month"));
        //router.get("/month").handler(routes.get("month"));

        return router;
    }
}
