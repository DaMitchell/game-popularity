package com.damitchell.tgp;

import com.damitchell.tgp.model.Game;
import com.damitchell.tgp.model.TopGame;
import com.damitchell.tgp.model.TopGames;
import com.damitchell.tgp.service.TwitchGameService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Task implements Runnable
{
    private static final Logger logger = LogManager.getLogger(Task.class);

    private static final String INSERT_GAME = "INSERT INTO game(id, name, box_small, box_medium, box_large, created) VALUES (:id, :name, :box_small, :box_medium, :box_large, :created)";
    private static final String INSERT_GAME_STATS = "INSERT INTO game_stats(id, date, viewers, channels) VALUES (:id, :date, :viewers, :channels)";
    private static final String INSERT_RUN_STATS = "INSERT INTO run_stats(date, total_games, total_viewers, total_channels, games_created, stats_created, requests_made) VALUES (:date, :total_games, :total_viewers, :total_channels, :games_created, :stats_created, :requests_made)";

    protected Sql2o db;
    protected Retrofit retrofit;
    protected TwitchGameService service;

    protected int limit;
    protected ZonedDateTime date;

    protected int totalGames;
    protected int totalViewers;
    protected int totalChannels;
    protected int gamesCreated;
    protected int statsCreated;
    protected int requestsMade;

    protected List<Integer> includedInRun;

    public Task(Sql2o db, int limit)
    {
        this.db = db;
        this.limit = limit;

        retrofit = new Retrofit.Builder()
            .baseUrl("https://api.twitch.tv/kraken/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        service = retrofit.create(TwitchGameService.class);
    }

    @Override
    public void run()
    {
        includedInRun = new ArrayList<>();
        date = ZonedDateTime.now(ZoneOffset.UTC);

        totalGames = -1;
        totalViewers = 0;
        totalChannels = 0;
        gamesCreated = 0;
        statsCreated = 0;
        requestsMade = 0;

        logger.info("Starting run at {}", date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        int offset = 0;
        boolean run = true;

        try {
            while (run) {
                requestsMade++;

                if(makeRequest(offset) < 1) {
                    run = false;
                }

                offset += limit;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        try (Connection connection = db.beginTransaction()) {
            connection.createQuery(INSERT_RUN_STATS)
                .addParameter("date", date)
                .addParameter("total_games", includedInRun.size())
                .addParameter("total_viewers", totalViewers)
                .addParameter("total_channels", totalChannels)
                .addParameter("games_created", gamesCreated)
                .addParameter("stats_created", statsCreated)
                .addParameter("requests_made", requestsMade)
                .executeUpdate();

            connection.commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        logger.info("Finished run and took {} seconds", Duration.between(LocalTime.now(), date).abs().getSeconds());
    }

    /**
     * @param offset pagination stuffs
     * @return Return the number of games from the last request made.
     * @throws IOException
     */
    private int makeRequest(int offset) throws IOException
    {
        TopGames topGames = service.topGames(limit, offset).execute().body();

        if(totalGames < 0) {
            totalGames = topGames.getTotal();
        }

        topGames.getTopGames().forEach(this::handleGame);

        return topGames.getTopGames().size();
    }

    private void handleGame(TopGame topGame)
    {
        Game game = topGame.getGame();

        if(includedInRun.contains(game.getId())) {
            return;
        }

        totalViewers += topGame.getViewers();
        totalChannels += topGame.getChannels();
        //System.out.println(game.getId() + " " + game.getName() + " | " + topGame.getViewers() + " " + topGame.getChannels());

        // 1. Make sure game is in DB.
        try (Connection connection = db.beginTransaction()) {
            connection.createQuery(INSERT_GAME)
                .addParameter("id", game.getId())
                .addParameter("name", game.getName())
                .addParameter("box_small", game.getBox().get("small"))
                .addParameter("box_medium", game.getBox().get("medium"))
                .addParameter("box_large", game.getBox().get("large"))
                .addParameter("created", date)
                .executeUpdate();

            connection.commit();
            gamesCreated++;
        } catch (Exception e) {
            if(!e.getMessage().contains("Duplicate")) {
                logger.error(e.getMessage());
            }
        }

        // 2. Add data to stats table.
        try (Connection connection = db.beginTransaction()) {
            connection.createQuery(INSERT_GAME_STATS)
                .addParameter("id", game.getId())
                .addParameter("date", date)
                .addParameter("viewers", topGame.getViewers())
                .addParameter("channels", topGame.getChannels())
                .executeUpdate();

            connection.commit();
            statsCreated++;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        includedInRun.add(game.getId());
    }
}
