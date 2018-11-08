package io.sqooba.etl.csv

import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.scalatest.{FunSuite, Matchers}

class UserETLTest extends FunSuite with DataFrameSuiteBase with Matchers {

  test("end2end with test CSV data") {

    import spark.implicits._

    val userDataFilename: String = this.getClass.getResource("/csv/csv-test-data.csv").getPath

    val df = sqlContext.read
      .format("csv")
      .option("header", true)
      .option("inferSchema", "true") // Optional, default: false
      .load(userDataFilename)

    df.schema.fieldNames should contain("id")
    df.schema.fieldNames should contain("first_name")
    df.schema.fieldNames should contain("last_name")
    df.schema.fieldNames should contain("email")
    df.schema.fieldNames should contain("gender")
    df.schema.fieldNames should contain("ip_address")

    df.show(false)

    val fakeData = df.map(r => {
      val id: Integer = r.getInt(0)
      val firstName: String = r.getString(1)
      val lastName: String = r.getString(2)
      val email: String = r.getString(3)
      val gender: String = r.getString(4)
      val ipAddress: String = r.getString(5)

      User(id.intValue(), firstName, lastName, email, gender, ipAddress)

    }).filter(_.id != null)

    fakeData.show(false)

    val c = fakeData.filter(_.id > 100)

    c.show(false)
  }

}