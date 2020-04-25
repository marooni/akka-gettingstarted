package com.example.directorywatcher

import java.io.File

import org.scalatest.FunSuite

class FileListingTest extends FunSuite {

  test("FileListing should return a list of files in a directory") {
    val directory:  String = "/home/mario/data/tdx_ops_prod_18001/TCLOG_18001"
    val fileListing: FileListing = FileListing()
    val result : List[File] = fileListing.getListOfFiles(directory)
    println(result)
    assert(result.size > 0)
  }
}
