package com.damitchell.tgp.service;

import com.damitchell.tgp.model.GameModel;
import com.google.inject.Inject;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class GamesService extends AbstractService
{
    private static final String SELECT_TOTAL_GAMES = "SELECT COUNT(id) AS total FROM game";
    private static final String SELECT_GAMES = "SELECT id, name, box_small, box_medium FROM game LIMIT ? OFFSET ?";
    private static final String SELECT_GAME = "SELECT id, name, box_small, box_medium FROM game WHERE id = ?";
    private static final String SELECT_TOP_GAMES = "SELECT g.id, g.name, g.box_small, box_medium, MAX(gs.viewers) AS viewers " +
        "FROM game AS g JOIN game_stats AS gs ON g.id = gs.id WHERE gs.date > ? GROUP BY g.id ORDER BY viewers DESC LIMIT 20";
    private static final String SELECT_GAME_BY_NAME = "SELECT g.id, g.name, g.box_small, box_medium FROM game AS g WHERE name LIKE ? ORDER BY name ASC LIMIT 20";

    @Inject
    public GamesService(JDBCClient client)
    {
        this.setClient(client);
    }

    public Observable<Integer> getTotalGames()
    {
        return query(SELECT_TOTAL_GAMES)
            .map(resultSet -> resultSet.getRows().get(0).getInteger("total"));
    }

    public Observable<List<GameModel>> getTopGames()
    {
        //TODO the minus 1 hour is just for local
        JsonArray params = new JsonArray().add(LocalDateTime.now().minusHours(1).minusMinutes(15)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return queryWithParams(SELECT_TOP_GAMES, params)
            .map(resultSet -> resultSet.getRows().stream()
                .map(GameModel::fromJsonObject)
                .collect(Collectors.toList()));
    }

    public Observable<List<GameModel>> getGamesByName(String name)
    {
        JsonArray params = new JsonArray().add("%" + name + "%");

        return queryWithParams(SELECT_GAME_BY_NAME, params)
            .map(resultSet -> resultSet.getRows().stream()
                .map(GameModel::fromJsonObject)
                .collect(Collectors.toList()));
    }

    public Observable<List<GameModel>> getGames(Integer limit, Integer offset)
    {
        JsonArray params = new JsonArray().add(limit).add(offset);

        return queryWithParams(SELECT_GAMES, params)
            .map(resultSet -> resultSet.getRows().stream()
                .map(GameModel::fromJsonObject)
                .collect(Collectors.toList()));
    }

    public Observable<GameModel> getGame(Integer id)
    {
        JsonArray params = new JsonArray().add(id);

        return queryWithParams(SELECT_GAME, params)
            .map(resultSet -> GameModel.fromJsonObject(resultSet.getRows().get(0)));
    }
}
