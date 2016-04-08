package com.damitchell.tgp;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Future;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.core.AbstractVerticle;

public class ApiVerticle extends AbstractVerticle
{
    private HttpServer httpServer;

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        Injector injector = Guice.createInjector(new Module(vertx));

        Router router = injector.getInstance(Router.class);

        httpServer = vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(8080, res -> startFuture.complete());
    }

    @Override
    public void stop(Future fut)
    {
        httpServer.close(res -> fut.complete());
    }
}
