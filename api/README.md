## API
This is what will provide the frontend the data collected by the worker. Its just a fairly simple number of endpoint
that will serve data from a database, nothing fancy.

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