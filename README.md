Ingestion Example
====

This project is demonstrating how to connect to a MSSQL database, extract a table,
convert the data in [AVRO](https://avro.apache.org) format and store the resulting file
in a local folder.

# Prerequisites

- Java 8
- Maven (optional, docker can be used instead, see below)
- Docker and docker-compose (for end-to-end test)

# End-to-end test with MSSQL

A `docker-compose.yml` file is provided to do an end-to-end test.
The docker container will start MSSQL, create a table and load
some test data in it.

The docker container configuration is taken from https://cardano.github.io/blog/2017/11/15/mssql-docker-container

Start the MSSQL container using `docker-compose`: 

```
docker-compose up -d mssql
```

Wait a few seconds, and your container is running, listening on port 1433 with some pre-loaded data.

Please refer to [mssql-docker](https://github.com/Microsoft/mssql-docker) documentation
if you need more details.


# Build the project

The project is Maven based, running

```
mvn clean package
```

Note: if you don't have `maven` installed, you can build inside a docker container like this:

```
docker run -it -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-jdk-8 mvn clean install
```

Once built, the project produces a runnable `jar` with all the required dependencies
in `target` folder, namely `target/ingestion_example-1.0-SNAPSHOT-jar-with-dependencies.jar`

# Run it

Once built, you can run it quickly using the fat `jar`:

```
java -jar target/ingestion_example-1.0-SNAPSHOT-jar-with-dependencies.jar
```

If MSSQL container is running and the folder `/tmp/output` doesn't exist yet,
the application will run properly and create a new folder `/tmp/output` with
the following layout:

```
ls -1 /tmp/output/
_SUCCESS
part-00000-e26a5368-12c8-44d5-9fe7-2366e8d91676.avro
```

The file `part-00000-e26a5368-12c8-44d5-9fe7-2366e8d91676.avro` is your new AVRO file which
can be shared with other teams.
