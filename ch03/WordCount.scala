// All the necessary imports
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._

// We create the SparkConf object
val conf = 
newSparkConf().setMaster("local").setAppName("exampleApp")

// We create the SparkContext from SparkConf
val sc = new SparkContext(conf)

// First, we create a RDD with the don-quijote.txt file contents 
val docs = sc.textFile("don-quijote.txt")

// Convert the text of each line in lowercase
val lower = docs.map( line =>line.toLowerCase)

// Separate each text line in words (strings separated by spaces)
// As we already know, the split command flattens the arrays 
val words = lower.flatMap(line =>line.split("\\s+"))

// Create the tuples (word, frequency)
// Each word initial frequency is 1 
val counts = words.map(word => (word, 1))

// Group by word, the sum of frequencies (peace of cake, isn't?)
val freq = counts.reduceByKey(_ + _)

// Reverse the tuple (frequency, word) 
val invFreq = freq.map(_.swap)

// Take the 20 largest and prints 
invFreq.top(20).foreach(println)
