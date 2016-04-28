package com.damitchell.tgp.model;

import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StatModel
{
    private ZonedDateTime date;
    private int viewers;
    private int channels;

    public StatModel(String date, int viewers, int channels)
    {
        //TODO Adding an hour to the date because it looks like vert.x JDBC parses it off.
        this(ZonedDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME).plusHours(1), viewers, channels);
    }

    public StatModel(ZonedDateTime date, int viewers, int channels)
    {
        this.date = date.withSecond(0);
        this.viewers = viewers;
        this.channels = channels;
    }

    public ZonedDateTime getDate()
    {
        return date;
    }

    public int getViewers()
    {
        return viewers;
    }

    public int getChannels()
    {
        return channels;
    }

    public static StatModel fromJsonObject(JsonObject jsonObject)
    {
        return new StatModel(
            jsonObject.getString("date"),
            jsonObject.getInteger("viewers"),
            jsonObject.getInteger("channels")
        );
    }
}
