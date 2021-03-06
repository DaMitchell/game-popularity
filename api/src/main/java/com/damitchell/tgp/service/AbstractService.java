package com.damitchell.tgp.service;

import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.sql.ResultSet;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

abstract class AbstractService
{
    private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);

    protected JDBCClient client;

    public JDBCClient getClient()
    {
        return client;
    }

    public void setClient(JDBCClient client)
    {
        this.client = client;
    }

    protected Observable<ResultSet> query(String query)
    {
        return client.getConnectionObservable()
            .flatMap(conn -> conn.queryObservable(query)
                .doOnNext(rs -> conn.close()));
    }

    protected Observable<ResultSet> queryWithParams(String query, JsonArray params)
    {
        return client.getConnectionObservable()
            .flatMap(conn -> conn.queryWithParamsObservable(query, params)
                .doOnNext(rs -> conn.close(event -> logger.info("DB connection closed"))));
    }
}
