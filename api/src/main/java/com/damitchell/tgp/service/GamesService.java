package com.damitchell.tgp.service;

import com.damitchell.tgp.model.GameModel;
import com.google.inject.Inject;
import io.vertx.core.json.JsonArray;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Observable;

import java.util.List;
import java.util.stream.Collectors;

public class GamesService extends AbstractService
{
    private static final String SELECT_TOTAL_GAMES = "SELECT COUNT(id) AS total FROM game";
    private static final String SELECT_GAMES = "SELECT id, name, box_small FROM game LIMIT ? OFFSET ?";
    private static final String SELECT_GAME = "SELECT id, name, box_small FROM game WHERE id = ?";

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
