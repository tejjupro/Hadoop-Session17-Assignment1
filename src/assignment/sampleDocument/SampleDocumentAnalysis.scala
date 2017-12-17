/*
 * This Program is used to find the numbers of rows of data and number of words in the document.
 * 
 * The input file used has  delimited with '-' 
 * Problem Statement:
   1. Write a program to read a text file and print the number of rows of data in the document.
   2. Write a program to read a text file and print the number of words in the document.
   3. We have a document where the word separator is -, instead of space. Write a spark
   code, to obtain the count of the total number of words present in the document.
   
Input file used :

SampleDocument  :
This-is-my-first-assignment.
It-will-count-the-number-of-lines-in-this-document.
The-total-number-of-lines-is-3
*
*/

package assignment.sampleDocument

import org.apache.spark.SparkConf

import org.apache.spark.SparkContext

object SampleDocumentAnalysis {
  def main(args: Array[String]): Unit = {

    //creating an instance of SparkConf to provide the spark configurations.This will make spark to run in local mode
    val conf = new SparkConf().setAppName("Working with List ").setMaster("local")

    //Providing configuration parameter to SparkContext with an  instance of SparkConf
    val sc = new SparkContext(conf)

    //textFile method creates an RDD from the file specified as a parameter
    val txtFile = sc.textFile("/home/acadgild/sridhar_scala/assignmet17/sampleDocument")

    /*
     * zipWithIndex will create an index for each element of an rdd , here index for each line is created
     * keyBy rdd will create a key for each line(element) of rdd
     * count action will count the number of elements of rdd , so here all the no of index will be counted 
     * thereby getting the number of lines present in the file
     * 
     */
    val rowNum = txtFile.zipWithIndex().keyBy(col => col._2).keys.count()
    
    //print the no of rows present 
    println("The number of rows of data in the document is " + rowNum)
    
    //flatmap flattens the collection passed to it. Split the line with delimiter '-'
    val word = txtFile.flatMap(lines => (lines.split("-")))

    //Filter the contents present with only words not letters , by using filter rdd where it checks only for words
    val wordsOnly = word.filter(letters => letters.matches("^[a-zA-Z.]*"))

    //map the words with 1 using map rdd
    val mapWord = wordsOnly.map(words => (words, 1))

    //use reduceByKey rdd which is used for aggregating values by keys , where here words are keys and value 1 as their values
    val Countwords = mapWord.reduceByKey((accum, n) => accum + n)
    
    //Print the words with numbers of occurences
    Countwords.foreach(println)

  }
}