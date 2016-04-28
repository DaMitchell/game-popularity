package com.damitchell.tgp.service;

import com.damitchell.tgp.model.MostChannelsModel;
import com.damitchell.tgp.model.MostViewersModel;
import com.google.inject.Inject;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TotalService extends AbstractService
{
    private static final String SELECT_MOST_VIEWERS_DAY = "SELECT gs.id, g.name, g.box_small, g.box_medium, gs.date, gs.viewers FROM game_stats as gs LEFT JOIN game AS g ON gs.id = g.id WHERE gs.date > ? ORDER BY gs.viewers DESC LIMIT 1";
    private static final String SELECT_MOST_CHANNELS_DAY = "SELECT gs.id, g.name, g.box_small, g.box_medium, gs.date, gs.channels FROM game_stats as gs LEFT JOIN game AS g ON gs.id = g.id WHERE gs.date > ? ORDER BY gs.viewers DESC LIMIT 1";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum Type {
        DAY,
        WEEK,
        MONTH
    }

    @Inject
    public TotalService(JDBCClient client)
    {
        this.setClient(client);
    }

    public Observable<MostViewersModel> getGameWithMostViewers(TotalService.Type type)
    {
        JsonArray params = new JsonArray().add(getDateTime(type).format(DATE_TIME_FORMATTER));

        return queryWithParams(SELECT_MOST_VIEWERS_DAY, params)
            .map(resultSet -> MostViewersModel.fromJsonObject(resultSet.getRows().get(0)));
    }

    public Observable<MostChannelsModel> getGameWithMostChannels(TotalService.Type type)
    {
        JsonArray params = new JsonArray().add(getDateTime(type).format(DATE_TIME_FORMATTER));

        return queryWithParams(SELECT_MOST_CHANNELS_DAY, params)
            .map(resultSet -> MostChannelsModel.fromJsonObject((resultSet.getRows().get(0))));
    }

    private ZonedDateTime getDateTime(TotalService.Type type)
    {
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneOffset.UTC).withNano(0);

        switch (type) {
            case DAY:
                dateTime = dateTime.minusDays(1);
                break;
            case WEEK:
                dateTime = dateTime.minusWeeks(1);
                break;
            case MONTH:
                dateTime = dateTime.minusMonths(1);
                break;
        }

        return dateTime;
    }
}
