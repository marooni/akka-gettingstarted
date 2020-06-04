package com.example.json

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object UTCTimestampDecoder {

  private final val dateTimeFormatterIn = DateTimeFormatter.ofPattern("yyyy.DDD.HH.mm.ss.SSS")
  private final val dateTimeFormatterOut = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

  def decode(dateStr: String): String = {
    val dateTime = LocalDateTime.parse(dateStr, dateTimeFormatterIn)
    dateTime.format(dateTimeFormatterOut)
  }
}