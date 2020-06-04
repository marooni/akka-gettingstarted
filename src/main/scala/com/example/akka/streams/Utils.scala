package com.example.akka.streams

object Utils {

  def factors(number: Int): Seq[Int] = {
    var buf = scala.collection.mutable.ArrayBuffer.empty[Int]
    for (i <- 1 to number) {
      if (number % i == 0)
        buf += i
    }
    buf
  }
}
