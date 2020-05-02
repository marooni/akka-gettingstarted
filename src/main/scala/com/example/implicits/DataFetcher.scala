package com.example.implicits

trait DataFetcher {
  def fetchTelemetryParameterMibInfo(): String
}

case class DatabaseDataFetcher() extends DataFetcher {
  override def fetchTelemetryParameterMibInfo(): String =
    s">>> data fetched from database"
}

case class MockedDataFetcher() extends DataFetcher {
  override def fetchTelemetryParameterMibInfo(): String =
    s">>> this is mocked data"
}
