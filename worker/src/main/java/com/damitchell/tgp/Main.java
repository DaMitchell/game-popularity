package com.damitchell.tgp;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sql2o.Sql2o;

import java.io.File;

public class Main
{
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args)
    {
        File file = new File("./config.conf");
        Config config = file.exists() ? ConfigFactory.load(ConfigFactory.parseFile(file)) : ConfigFactory.load();

        logger.info("Starting worker");

        Sql2o sql2o = new Sql2o(String.format("jdbc:mysql://%s:%s/%s",
            config.getString("db.host"),
            config.getString("db.port"),
            config.getString("db.name")
        ), config.getString("db.username"), config.getString("db.password"));

        (new Task(sql2o, 100)).run();
    }
}

