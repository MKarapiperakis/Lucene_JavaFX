package jfx;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;





public class Indexer extends PorterStemmer
{
	public IndexWriter writer;
	 public Indexer(String indexDirectoryPath) throws IOException 
	 {
		 //this directory will contain the indexes
		 
		 //System.out.println(indexDirectoryPath);
		 Path indexPath = Paths.get(indexDirectoryPath);
		 if(!Files.exists(indexPath)) 		//IF THE FILE DOESN'T EXIST, WE CREATE IT
		 {
			 Files.createDirectory(indexPath);
		 }
		 //Path indexPath = Files.createTempDirectory(indexDirectoryPath);
		 Directory indexDirectory = FSDirectory.open(indexPath);
		 //create the indexer
		 IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		 //System.out.println("config is: " + config);
		 writer = new IndexWriter(indexDirectory, config);
		 
	 }
	 
	 public void close() throws CorruptIndexException, IOException 
	 {
		 writer.close();
	 }
	 
	 
	 
	 public void indexFile(File file) throws IOException 
	 {
		 String fileName = file.getName();
		 String Places = "";
		 String People = "";
		 String Title = "";
		 String Body = "";
		 
		 //if(fileName.equals("record1.txt") || fileName.equals("record2.txt")) 
		 //if(fileName.equals("record1.txt"))
		 //{
			 
			 	
			FileReader fileReader = new FileReader(file);
			int character;
			String word = "";
			while((character = fileReader.read()) != -1) {
				
				word = word + (char)character;
				if(word.contains("</PLACES>"))
				{
					word = word.replaceAll("</PLACES>","");
					word = word.replaceAll("<PLACES>","");
					Places = word;
					word = "";
				}
				
				if(word.contains("</PEOPLE>"))
				{
					word = word.replaceAll("</PEOPLE>","");
					word = word.replaceAll("<PEOPLE>","");
					People = word;
					word = "";
				}
				
				if(word.contains("</TITLE>"))
				{
					word = word.replaceAll("</TITLE>","");
					word = word.replaceAll("<TITLE>","");
					Title = word;
					word = "";
				}
				
				if(word.contains("</BODY>"))
				{
					word = word.replaceAll("</BODY>","");
					word = word.replaceAll("<BODY>","");
					Body = word;
					word = "";
				}
			}
			
			People = People.replace("\n", "").replace("\r", "");	//we remove whitespace and newline character
			Title = Title.replace("\n", "").replace("\r", "");		//we remove whitespace and newline character
			Title = Title.replaceAll("[^a-zA-Z0-9]", " ");  
			Title = Title.replaceAll("\\s+","");
			Title = Title.toLowerCase();
			Places = Places.replaceAll("[^a-zA-Z0-9]", "");
			//System.out.println("places: " + Places);
			//System.out.println("people: " + People);
			//System.out.println("Title: " + Title);
			//System.out.println("Body: " + Body);
			
			
			
			  Analyzer analyzer = new StandardAnalyzer();
		      TokenStream tokenStream = analyzer.tokenStream(LuceneConstants.CONTENTS, new StringReader(Body));
		      OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
		      CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		      tokenStream.reset();
		      final List<String> stop_Words = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by",
		    		  "for", "if", "in", "into", "is", "it",
		    		  "no", "not", "of", "on", "or", "such",
		    		  "that", "the", "their", "then", "there", "these",
		    		  "they", "this", "to", "was", "will", "with");
	 		  final CharArraySet stopSet = new CharArraySet(stop_Words, true);
	 		  tokenStream = new StopFilter(tokenStream, stopSet);
	 		 
	 		 
	 		  StringBuilder sb = new StringBuilder();
	 		  int k = 0;
		      while (tokenStream.incrementToken()) {
		          int startOffset = offsetAttribute.startOffset();
		          int endOffset = offsetAttribute.endOffset();
		          String term = charTermAttribute.toString();
		          
		         // System.out.println("Our term for stemming is " + term);
		          PorterStemmer stem = new PorterStemmer();
		          for(int i=0;i<term.length();i++)
		          {
		        	  stem.add(term.charAt(i));
		        	  
		          }
		          stem.stem();
		          
		          String term2 = stem.toString();

				 // System.out.println("After stemming our term is <" + term2 + ">");
		          
		        //We merge the terms, separating with ","
		          
		          if(k>0)
		        	  word = word + "," + term2;	
		          else
		        	  word = word + term2;
			      
			     k++;
			     
		         // System.out.println(term2);
		         
		      }
		      //System.out.println(word);
		      Document document = new Document();
		      
		      
			  Field bodyField = new Field(LuceneConstants.BODY, word,TextField.TYPE_STORED);
			  Field titleField = new Field(LuceneConstants.TITLE, Title,StringField.TYPE_STORED);
			  Field peopleField = new Field(LuceneConstants.PEOPLE, People,TextField.TYPE_STORED);
			  Field placesField = new Field(LuceneConstants.PLACES, Places,TextField.TYPE_STORED);
			  String FileName = file.getName();
			  Field FileNameField = new Field(LuceneConstants.FILE_NAME, FileName,TextField.TYPE_STORED);
			  
			  
		      document.add(bodyField);
		      document.add(titleField);
		      document.add(peopleField);
		      document.add(placesField);
		      document.add(FileNameField);
		      
		      writer.addDocument(document);
		     fileReader.close();
		    
		 //}
	 }
	 
	 public int createIndex(String dataDirPath, FileFilter filter) throws IOException 
	 {
		 //get all files in the data directory
		
		 File[] files = new File(dataDirPath).listFiles();
		 for (File file : files) 
		 {
			 if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file))
			 {
				 indexFile(file);
			 }
		 }
		 
	 return writer.numRamDocs();
	 }

	 
	 
	 
	 
	 
}
