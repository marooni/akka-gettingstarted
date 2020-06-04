package com.example.json

import java.io.File
import java.nio.file.{Files, Paths}

import scala.collection.JavaConverters._
import akka.NotUsed
import akka.stream.scaladsl.Source

import scala.util.Try

object Sources {

  def telemetryParameterAsJson(basePath: String): Source[ParameterFile, NotUsed] = {

    val baseDir = new File(basePath)

    Source.fromIterator(() =>
      Files
        .walk(Paths.get(basePath))
        .iterator()
        .asScala
        .map { path =>
          val name = path.getFileName.toString.substring(0, path.getFileName.toString.lastIndexOf('.'))
          val list = Files
            .lines(path)
            .iterator()
            .asScala
            .drop(1)
            .map { line =>
              Try {
                val columns = line.split(",")
                val t = columns(0)
                val v = columns(1)
                //val isADouble = Try(value.toDouble).isSuccess && value.toDouble.<=(Double.MaxValue)
                ParameterValue(UTCTimestampDecoder.decode(t), v)
              }.toEither match {
                case Left(error) =>
                  println(s"Extraction of data from file $name failed with message: ${error.getMessage}")
                  ParameterValue("", "")
                case Right(parameterValue) =>
                  parameterValue
              }
            }
            .toList

          list.headOption match {
            case Some(p) => ParameterFile(p.utc, name, list)
            case None => ParameterFile("", "", List.empty[ParameterValue])
          }
        }
    )
  }

  def telemetryParameter(basePath: String): Source[TelemetryParameter, NotUsed] = {

    val doy = 1
    Source.fromIterator(() =>
      Files
        .walk(Paths.get(basePath))
        .iterator()
        .asScala
        .flatMap { path =>
          val name = path.getFileName.toString.substring(0, path.getFileName.toString.lastIndexOf('.'))
          Files
            .lines(path)
            .iterator()
            .asScala
            .drop(1)
            .map { line =>
              val columns = line.split(",")
              val t = columns(0)
              val v = columns(1)
              TelemetryParameter(doy, name, t, v)
            }
        }
    )
  }

  def telemetryParameter2(basePath: String): Source[TelemetryParameter, NotUsed] = {
    val doy = 1
    Source.fromIterator(() =>
      Files
        .walk(Paths.get(basePath))
        .iterator()
        .asScala
        .map { path =>
          val name = path.getFileName.toString.substring(0, path.getFileName.toString.lastIndexOf('.'))
          TelemetryParameter(doy, name, "", "")
        }
    )
  }
}


