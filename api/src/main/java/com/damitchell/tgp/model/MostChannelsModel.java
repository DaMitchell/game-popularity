package com.damitchell.tgp.model;

import com.theoryinpractise.halbuilder.api.Representation;
import io.vertx.core.json.JsonObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MostChannelsModel
{
    private GameModel game;
    private ZonedDateTime date;
    private int channels;

    public MostChannelsModel(GameModel game, String date, int channels)
    {
        //TODO Adding an hour to the date because it looks like vert.x JDBC parses it off.
        this(game, ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).plusHours(1), channels);
    }

    public MostChannelsModel(GameModel game, ZonedDateTime date, int channels)
    {
        this.game = game;
        this.date = date.withSecond(0);
        this.channels = channels;
    }

    public GameModel getGame()
    {
        return game;
    }

    public ZonedDateTime getDate()
    {
        return date;
    }

    public int getChannels()
    {
        return channels;
    }

    public Representation getRep(Representation rep)
    {
        return rep.withProperty("date", date.format(DateTimeFormatter.ISO_INSTANT))
            .withProperty("channels", channels);
    }

    public static MostChannelsModel fromJsonObject(JsonObject jsonObject)
    {
        return new MostChannelsModel(
            GameModel.fromJsonObject(jsonObject),
            jsonObject.getString("date"),
            jsonObject.getInteger("channels")
        );
    }
}
