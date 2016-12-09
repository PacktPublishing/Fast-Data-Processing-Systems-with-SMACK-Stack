
val conf = new SparkConf(false)
  .setMaster("local[*]")
  .setAppName("Spark Streaming with Akka")
  .set("spark.logConf", "true")
  .set("spark.driver.port", s"$driverPort")
  .set("spark.driver.host", s"$driverHost")
  .set("spark.akka.logLifecycleEvents", "true")
val ssc = new StreamingContext(conf, Seconds(1))
        
val actorName = "salutator"
val actorStream: ReceiverInputDStream[String] = ssc.actorStream[String](Props[Salutator], actorName)
        
actorStream.print()      
ssc.start()

val actorSystem = SparkEnv.get.actorSystem
val url = s"akka.tcp://spark@$driverHost:$driverPort/user/Supervisor0/$actorName"
val timeout = 100 seconds
val salutator =    
  Await.result(actorSystem.actorSelection(url).resolveOne(timeout), timeout)
salutator ! "Hello"
salutator ! "from"
salutator ! "Apache Spark"
salutator ! "and Akka"
