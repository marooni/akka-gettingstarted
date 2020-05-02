package com.example.implicits

case class DataConnector() {

  println("DatabaseConnector created")

  def getData()(implicit dataFetcher: DataFetcher): String =
    dataFetcher.fetchTelemetryParameterMibInfo()

}
