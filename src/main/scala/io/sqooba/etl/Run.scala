package io.sqooba.etl

import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object Run extends StrictLogging {


  val sparkConf = new SparkConf
  sparkConf.setJars(SparkContext.jarOfObject(this.getClass).toList)

  val sparkSession = SparkSession
    .builder()
    .config(sparkConf)
    .appName("-Ingestion-Pipeline")
    .enableHiveSupport()
    .getOrCreate()


  // ...
}
