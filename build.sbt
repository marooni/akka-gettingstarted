
lazy val akkagettingstarted = project
  .in(file(""))
  .settings(
   	inThisBuild(
      Seq(
        organization := "com.example",
        organizationName := "DLR GSOC",
        scalaVersion := "2.12.8",
        version := "0.1"
      )
    )  
  )
  .settings(
    name := "akka-gettingstarted",
    description := "akka-gettingstarted",
    libraryDependencies ++= List(
      Dependencies.`akka-stream-typed`,
      Dependencies.`akka-stream`,
      Dependencies.`spray-json`,
      Dependencies.`slick`,
      Dependencies.`postgresqlDriver`,
      Dependencies.scalatest % Test,
      Dependencies.logging,
      Dependencies.logback,
      Dependencies.`akka-typed`,
      Dependencies.`akka-testkit-typed`
    )    
  )
  .enablePlugins(JavaAppPackaging)
