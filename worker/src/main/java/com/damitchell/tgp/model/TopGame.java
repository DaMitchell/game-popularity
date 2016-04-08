package com.damitchell.tgp.model;

public class TopGame
{
    protected int viewers;
    protected int channels;

    protected Game game;

    public TopGame(int viewers, int channels, Game game)
    {
        this.viewers = viewers;
        this.channels = channels;
        this.game = game;
    }

    public int getViewers()
    {
        return viewers;
    }

    public int getChannels()
    {
        return channels;
    }

    public Game getGame()
    {
        return game;
    }
}
