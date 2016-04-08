package com.damitchell.tgp.model;

import io.vertx.core.json.JsonObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatModel
{
    private LocalDateTime date;
    private int viewers;
    private int channels;

    public StatModel(String date, int viewers, int channels)
    {
        // 2016-03-24T19:30:03Z
        this(LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME), viewers, channels);
    }

    public StatModel(LocalDateTime date, int viewers, int channels)
    {
        this.date = date;
        this.viewers = viewers;
        this.channels = channels;
    }

    public static StatModel fromJsonObject(JsonObject jsonObject)
    {
        return new StatModel(
            jsonObject.getString("date"),
            jsonObject.getInteger("viewers"),
            jsonObject.getInteger("channels")
        );
    }

    public LocalDateTime getDate()
    {
        //format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm::00"))
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
}
