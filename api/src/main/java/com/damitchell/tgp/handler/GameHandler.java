package com.damitchell.tgp.handler;

import com.google.inject.Inject;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import io.vertx.core.Handler;
import io.vertx.rxjava.core.http.HttpServerRequest;
import io.vertx.rxjava.ext.web.RoutingContext;

public class GameHandler implements Handler<RoutingContext>
{
    private RepresentationFactory factory;

    @Inject
    public GameHandler(RepresentationFactory factory)
    {
        this.factory = factory;
    }

    @Override
    public void handle(RoutingContext context)
    {
        //TODO Once ready this handler will take custom "from" and "to" dates.
        HttpServerRequest request = context.request();

        String uri = request.absoluteURI();

        Representation rep = factory.newRepresentation(uri);
        rep.withLink("day", uri + "/day");
        rep.withLink("week", uri + "/week");
        rep.withLink("month", uri + "/month");

        context.response()
            .putHeader("content-type", "application/hal+json")
            .end(rep.toString(RepresentationFactory.HAL_JSON));
    }

}
