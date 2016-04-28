package com.damitchell.tgp.model;

import com.theoryinpractise.halbuilder.api.Representation;
import io.vertx.core.json.JsonObject;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MostViewersModel
{
    private GameModel game;
    private ZonedDateTime date;
    private int viewers;

    public MostViewersModel(GameModel game, String date, int viewers)
    {
        //TODO Adding an hour to the date because it looks like vert.x JDBC parses it off.
        this(game, ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).plusHours(1), viewers);
    }

    public MostViewersModel(GameModel game, ZonedDateTime date, int viewers)
    {
        this.game = game;
        this.date = date.withSecond(0);
        this.viewers = viewers;
    }

    public GameModel getGame()
    {
        return game;
    }

    public ZonedDateTime getDate()
    {
        return date;
    }

    public int getViewers()
    {
        return viewers;
    }

    public Representation getRep(Representation rep)
    {
        return rep.withProperty("date", date.format(DateTimeFormatter.ISO_INSTANT))
            .withProperty("viewers", viewers);
    }

    public static MostViewersModel fromJsonObject(JsonObject jsonObject)
    {
        return new MostViewersModel(
            GameModel.fromJsonObject(jsonObject),
            jsonObject.getString("date"),
            jsonObject.getInteger("viewers")
        );
    }
}
