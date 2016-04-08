package com.damitchell.tgp.model;

import com.theoryinpractise.halbuilder.api.Representation;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class GameModel
{
    private int id;
    private String name;
    private Map<String, String> boxes = new HashMap<>();

    public GameModel(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public GameModel addBox(String size, String value)
    {
        boxes.put(size, value);
        return this;
    }

    public Representation getRep(Representation rep)
    {
        return rep.withProperty("id", id)
            .withProperty("name", name)
            .withProperty("box", boxes);
    }

    public static GameModel fromJsonObject(JsonObject jsonObject)
    {
        return new GameModel(jsonObject.getInteger("id"), jsonObject.getString("name"))
            .addBox("small", jsonObject.getString("box_small"));
    }

    public int getId()
    {
        return id;
    }

    public Map<String, String> getBoxes()
    {
        return boxes;
    }

    public String getName()
    {
        return name;
    }
}
