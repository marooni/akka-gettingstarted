package com.example.akka.streams

import akka.NotUsed
import akka.stream.scaladsl.Source

object Sources {

  def naturalNumbers(): Source[Int, NotUsed] = {
    Source(1 to 100)
  }

  def factorials(): Source[BigInt, NotUsed] = {
    Sources
      .naturalNumbers()
      .scan(BigInt(1))((acc, next) => acc * next)
  }

  def integerStrings(): Source[String,NotUsed] = {
    Sources
      .naturalNumbers()
      .map(i => s"$i")
  }

  def fizzBuzz(words: (String, String)): Source[String,NotUsed] = {
    Sources
      .naturalNumbers()
      .map { i =>
        (i % 3, i % 5) match {
          case (0, 0) => List(words._1, words._2).mkString(" ")
          case (0, _) => words._1
          case (_, 0) => words._2
          case _ => s"$i"
        }
      }
  }

  def factors(): Source[Seq[Int], NotUsed] = {
    Sources
      .naturalNumbers()
      .map(Utils.factors)
  }

  def primeNumbers(): Source[Int,NotUsed] = {
    Sources
      .factors()
      .filter(factors => factors.size == 2)
      .map(seq => seq.takeRight(1).head)
  }
}
