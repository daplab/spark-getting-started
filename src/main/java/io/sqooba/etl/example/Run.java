package io.sqooba.etl.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.execution.datasources.jdbc.JDBCOptions;


/**
 * Fetch data in a MSSQL database, convert it in AVRO and store in the local filesystem.
 * Please have a look at the provided README.md file to run a MSSQL instance.
 */
public class Run {


    private static final String SPARK_LOCAL_MASTER = "local";
    private static final String SPARK_APP_NAME = "mssql-to-avro-file-example";

    private static final String JDBC_DATABASE = "Ingestion";
    private static final String JDBC_USERNAME = "SA";
    private static final String JDBC_PASSWORD = "P@55w0rd";
    private static final String JDBC_URL = "jdbc:sqlserver://127.0.0.1:1433;database=" + JDBC_DATABASE + ";user=" + JDBC_USERNAME + ";password=" + JDBC_PASSWORD;
    private static final String JDBC_TABLE = "MyTable1";



    public static void main(String[] args) {

        // Instantiate a Spark context. We will run Spark in local mode here
        // so we don't have any dependency on Hadoop.
        SparkSession spark = SparkSession
                .builder()
                .appName(SPARK_APP_NAME)
                .master(SPARK_LOCAL_MASTER)
                .getOrCreate();



        // Connecting to the MSSQL database.
        // Any database is usable here, as long as you have the jdbc driver.
        // Please mind the comment on {@link getTableName} function.
        Dataset<Row> ds = spark.read()
                .format("jdbc")
                .option(JDBCOptions.JDBC_URL(), JDBC_URL)
                .option(JDBCOptions.JDBC_TABLE_NAME(), getTableName(JDBC_DATABASE, JDBC_TABLE))
                .option(JDBCOptions.JDBC_DRIVER_CLASS(), "com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .load();



        // The JDBC connector is fetching the schema and keeping it the the underlying Dataset.
        ds.printSchema();




        // This magic line
        // 1) ensures one single file is created as output. This part is optional.
        // 2) converts the Spark Dataset to AVRO
        // 3) Write the .avro files in an output folder in the local filesystem.
        // Please not that writing to S3 is possible directly from here.
        ds.coalesce(1).write().format("com.databricks.spark.avro").save("/tmp/output");


    }

    private static String getTableName(String db, String table) {
        return getTableName(db, table, null);
    }

    // This function is a work around current Spark version
    // which is not pushing down predicates how you think it should.
    private static String getTableName(String db, String table, Integer limit) {
        if (limit == null) {
            return table;
        } else {
            return "(SELECT * FROM " + getTableName(db, table) + " LIMIT " + limit + ") as " + table;
        }
    }
}
