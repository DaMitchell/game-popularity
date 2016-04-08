## Worker
A small java app that will query the twitch API to pull down the stats for the games that are being streamed. The URL
that it will query is [https://api.twitch.tv/kraken/games/top](https://api.twitch.tv/kraken/games/top). At the minute it
is setup to run every 15 minutes via a cron.

## Environment Variables
Just to note that it is setup to use a MySQL/MariaDB database.

- **TGP_DB_HOST**
   The address of the database the worker should be connecting to. Running outside a docker container this would be a 
   normal address, but inside a container it should be the name of the database container on the same docker network 
   ([https://docs.docker.com/engine/userguide/networking/](https://docs.docker.com/engine/userguide/networking/)).
- **TGP_DB_PORT**
   The port of the database being connected to.
- **TGP_DB_NAME**
   The name of the database to use.
- **TGP_DB_USERNAME**
   A username to connect with.
- **TGP_DB_PASSWORD**
   And lastly the password to use