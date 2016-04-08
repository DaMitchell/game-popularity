package com.damitchell.tgp.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.RoutingContext;

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

        router.get("/games").handler(routes.get("games"));
        router.get("/games/:id").handler(routes.get("game"));

        return router;
    }
}
