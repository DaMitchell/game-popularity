package com.damitchell.tgp.model;

import java.util.List;

public class TopGames
{
    protected int _total;
    protected List<TopGame> top;

    public TopGames(int _total, List<TopGame> top)
    {
        this._total = _total;
        this.top = top;
    }

    public int getTotal()
    {
        return _total;
    }

    public List<TopGame> getTopGames()
    {
        return top;
    }
}
