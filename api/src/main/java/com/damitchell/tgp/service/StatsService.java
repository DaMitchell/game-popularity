package com.damitchell.tgp.service;

import com.damitchell.tgp.model.StatModel;
import com.google.inject.Inject;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class StatsService extends AbstractService
{
    private static final String SELECT_STATS_GAME = "SELECT date, viewers, channels FROM game_stats WHERE id = ? AND date BETWEEN ? AND ?";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00");

    @Inject
    public StatsService(JDBCClient client)
    {
        this.setClient(client);
    }

    public Observable<List<StatModel>> getStatsForGame(Integer id, ChronoLocalDate from, ChronoLocalDate to)
    {
        JsonArray params = new JsonArray()
            .add(id)
            .add(from.format(DATE_TIME_FORMATTER))
            .add(to.format(DATE_TIME_FORMATTER));

        return queryWithParams(SELECT_STATS_GAME, params)
            .map(resultSet -> resultSet.getRows().stream()
                .map(StatModel::fromJsonObject)
                .collect(Collectors.toList()));
    }
}
