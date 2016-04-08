package com.damitchell.tgp.model;

import java.util.Map;

public class Game
{
    protected int _id;

    protected String name;
    protected Map<String, String> box;

    public Game(int _id, String name, Map<String, String> box)
    {
        this._id = _id;
        this.name = name;
        this.box = box;
    }

    public int getId()
    {
        return _id;
    }

    public String getName()
    {
        return name;
    }

    public Map<String, String> getBox()
    {
        return box;
    }
}
