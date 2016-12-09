// Create a StreamingContext with a the SparkConf
val scontext = new StreamingContext(sparkConf, Seconds(1))

// Create a DStream to connect to server at IP and Port
val lines = scontext.socketTextStream(serverIP, serverPort)

// Count each word in each batch
val words = lines.flatMap(_.split(" "))
val pairs = words.map( word => (word, 1) )
val counts = pairs.reduceByKey(_ + _)

// Print the count to the console, the start() method is to begin the execution
counts.print()
scontext.start()  
