package io.sqooba.etl.csv

case class User(
                 id: Integer,
                 first_name: String,
                 last_name: String,
                 email: String,
                 gender: String,
                 ip_address: String
)
