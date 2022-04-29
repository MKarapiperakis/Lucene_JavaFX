package jfx;



import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;


public class LuceneTester 
{
	String indexDir = "index\\";
	String dataDir = "data\\";
	Indexer indexer;
	Searcher searcher;
	ObservableList<String> l1 = FXCollections.observableArrayList();
	ObservableList<String> l2 = FXCollections.observableArrayList();
	String keyword = "";
	String keyword2 = "";
	String Sfield = "";
	String Sfield2 = "";
	 
	ClassicSimilarity sim = new ClassicSimilarity();
	
	 
     public void start()
	 {
		 LuceneTester tester;
		 try 
		 {
			 tester = new LuceneTester();
			 tester.createIndex();
			 //tester.search("year");
		 } 
		 catch (IOException e) 				//TRY-CATCH FOR THE METHOD "createIndex()" AND THE METHOS "search()
		 {
			 e.printStackTrace();
	
		 } 
		
	 }
	 
	 public void createIndex() throws IOException 
	 {
		 indexer = new Indexer(indexDir);	//we send the index Directory
		 int numIndexed;
		 long startTime = System.currentTimeMillis();						//START TIME
		 numIndexed = indexer.createIndex(dataDir, new TextFileFilter());	//WE COUNT THE FILES INSIDE THE FOLDER "Data"
		 long endTime = System.currentTimeMillis();							//END TIME
		 indexer.close();
		 System.out.println(numIndexed+" File(s) indexed, time taken: " + (endTime-startTime)+" ms");
	 }

	 public void search(String searchQuery,String field) throws IOException,ParseException 
	 {
		keyword = searchQuery;
		Sfield = field;
		 searcher = new Searcher(indexDir,field);					//WE CALL THE CONNSTRUCTOR SENDING THE PATH
		 long startTime = System.currentTimeMillis();		
		 if(field == "body")
		 {
			PorterStemmer stem = new PorterStemmer();
			for(int i=0;i<searchQuery.length();i++)
			{
				stem.add(searchQuery.charAt(i));
				
			}
			stem.stem();
			
			searchQuery = stem.toString();

		 }
		 if(field == "title")
		 {
			searchQuery = searchQuery.replace("\n", "").replace("\r", "");		//we remove whitespace and newline character
			searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]", " ");  
			searchQuery = searchQuery.replaceAll("\\s+","");
			searchQuery = searchQuery.toLowerCase();
		 }
		 if(field == "places")
		 {
			searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]", "");
		 }
		 //System.out.println(searchQuery);
		 TopDocs hits = searcher.search(searchQuery);
		 long endTime = System.currentTimeMillis();			
		
		 l2.clear();
		
		 
		 
		 
		// System.out.println(hits.totalHits +" documents found. Time :" + (endTime - startTime));
		 
		l1.clear();
		 for(ScoreDoc scoreDoc : hits.scoreDocs) 
		 {
			 Document doc = searcher.getDocument(scoreDoc);
			 //System.out.println("File: " + doc.get(LuceneConstants.FILE_NAME));
			 l2.add(String.valueOf(scoreDoc.score)); 
			 l1.add(doc.get(LuceneConstants.FILE_NAME));

		 }
		 searcher.close();
		
	 }

	 public void search(String searchQuery,String searchQuery2,String field1,String field2,String op1,String op2) throws IOException,ParseException 
	 {
		keyword = searchQuery;
		keyword2 = searchQuery2;
		Sfield = field1;
		Sfield2 = field2;
		QueryParser queryParser;
		Query q;
		 searcher = new Searcher(indexDir,field1);					//WE CALL THE CONNSTRUCTOR SENDING THE PATH
		 long startTime = System.currentTimeMillis();		
		 Term term1 = new Term("","");
		 Term term2 = new Term("","");
		if(field1 == "body")
		{
		 	 searchQuery = searchQuery.toLowerCase();
			  PorterStemmer stem = new PorterStemmer();
			for(int i=0;i<searchQuery.length();i++)
			{
				stem.add(searchQuery.charAt(i));
				
			}
			stem.stem();
			
			searchQuery = stem.toString();
			term1 = new Term(LuceneConstants.BODY, searchQuery);
		}
		else if(field1 == "people")
		{
			searchQuery = searchQuery.toLowerCase();
			 term1 = new Term(LuceneConstants.PEOPLE, searchQuery);
		}
		else if(field1 == "title")
		{
			searchQuery = searchQuery.replace("\n", "").replace("\r", "");		//we remove whitespace and newline character
			searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]", " ");  
			searchQuery = searchQuery.replaceAll("\\s+","");
			searchQuery = searchQuery.toLowerCase();
			 term1 = new Term(LuceneConstants.TITLE, searchQuery);
		}
		else if(field1 == "places")
		{
			 searchQuery = searchQuery.toLowerCase();
			 searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]", "");
			 term1 = new Term(LuceneConstants.PLACES, searchQuery);
			
		}

		if(field2 == "body")
		{
			searchQuery2 = searchQuery2.toLowerCase();
			 PorterStemmer stem = new PorterStemmer();
			for(int i=0;i<searchQuery2.length();i++)
			{
				stem.add(searchQuery2.charAt(i));
				
			}
			stem.stem();
			
			searchQuery2 = stem.toString();
			term2 = new Term(LuceneConstants.BODY, searchQuery2);
		}
		else if(field2 == "people")
		{
			searchQuery2 = searchQuery2.toLowerCase();
			 term2 = new Term(LuceneConstants.PEOPLE, searchQuery2);
		}
		else if(field2 == "title")
		{
			searchQuery2 = searchQuery2.replace("\n", "").replace("\r", "");		//we remove whitespace and newline character
			searchQuery2 = searchQuery2.replaceAll("[^a-zA-Z0-9]", " ");  
			searchQuery2 = searchQuery2.replaceAll("\\s+","");
			searchQuery2 = searchQuery2.toLowerCase();
			 term2 = new Term(LuceneConstants.TITLE, searchQuery2);
		}
		else if(field2 == "places")
		{
			searchQuery2 = searchQuery2.toLowerCase();
			searchQuery2 = searchQuery2.replaceAll("[^a-zA-Z0-9]", "");
			 term2 = new Term(LuceneConstants.PLACES, searchQuery2);
			
		}
     	 
		 Query query1 = new TermQuery(term1);
		 Query query2 = new TermQuery(term2);
		
		

		 BooleanQuery.Builder query = new BooleanQuery.Builder();

		 if(op1 == "MUST")
		 {
			query.add(query1,BooleanClause.Occur.MUST);
		 }
		 else if(op1 == "SHOULD")
		 {
			query.add(query1,BooleanClause.Occur.SHOULD);
		 }
		 else if(op1 == "NOT")
		 {
			query.add(query1,BooleanClause.Occur.MUST_NOT);
		 }

		 if(op2 == "MUST")
		 {
			query.add(query2,BooleanClause.Occur.MUST);
		 }
		 else if(op2 == "SHOULD")
		 {
			query.add(query2,BooleanClause.Occur.SHOULD);
		 }
		 else if(op2 == "NOT")
		 {
			query.add(query2,BooleanClause.Occur.MUST_NOT);
		 }
      	

		
		   
		TopDocs hits = searcher.search(query);
		long endTime = System.currentTimeMillis();		
		 
		l2.clear();
		l1.clear();
		 //System.out.println(hits.totalHits +" documents found. Time :" + (endTime - startTime));
		 
		
		 for(ScoreDoc scoreDoc : hits.scoreDocs) 
		 {
			 Document doc = searcher.getDocument(scoreDoc);
			// System.out.println("File: " + doc.get(LuceneConstants.FILE_NAME));
			 l2.add(String.valueOf(scoreDoc.score)); 
			 l1.add(doc.get(LuceneConstants.FILE_NAME));
		 }
		 searcher.close();
		
	 }
	 IndexReader indexReader;
	 
	 public void FileCompare(String FileQuery) throws IOException,ParseException 
	 {
		File file2 = new File("Data//" + FileQuery);
		FileReader fileReader = new FileReader(file2);
			int character;
			String word = "";
			String Title = "";
			String Body = "";
			while((character = fileReader.read()) != -1) 
			{
				
				word = word + (char)character;
				
				
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
			Term term1 = new Term("","");
			BooleanQuery.Builder query = new BooleanQuery.Builder();
			searcher = new Searcher(indexDir,"body");
			while (tokenStream.incrementToken()) 
			{
				String term = charTermAttribute.toString();
				term1 = new Term(LuceneConstants.BODY, term);
				Query query1 = new TermQuery(term1);

				
				query.add(query1,BooleanClause.Occur.SHOULD);
			}
		
			
			TopDocs hits = searcher.search(query);
			l2.clear();
			l1.clear();
			for(ScoreDoc scoreDoc : hits.scoreDocs) 
			{
				Document doc = searcher.getDocument(scoreDoc);
				l2.add(String.valueOf(scoreDoc.score)); 
			 	l1.add(doc.get(LuceneConstants.FILE_NAME));
			}
			searcher.close();
	 }

	 public void PhraseSearch(String searchQuery) throws IOException,ParseException 
	 {
		
		searcher = new Searcher(indexDir,"body");
		keyword = searchQuery;
		PhraseQuery.Builder query = new PhraseQuery.Builder();
		String[] words = searchQuery.split(" ");
		PorterStemmer stem = new PorterStemmer();
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream(LuceneConstants.CONTENTS, new StringReader(searchQuery));
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

		

		while (tokenStream.incrementToken()) {
			int startOffset = offsetAttribute.startOffset();
			int endOffset = offsetAttribute.endOffset();
			String term = charTermAttribute.toString();
			
			for(int i=0;i<term.length();i++)
			{
				stem.add(term.charAt(i));	
			}
			stem.stem();
			term = stem.toString();
			//System.out.println(term);
			query.add(new Term("body", term));
		}

		
		
		TopDocs hits = searcher.Phsearch(query);

		l2.clear();
		
		 
		 
		 
		l1.clear();
		
		//System.out.println(hits.totalHits +" documents found.");

		for(ScoreDoc scoreDoc : hits.scoreDocs) 
		 {
			 Document doc = searcher.getDocument(scoreDoc);
			 //System.out.println("File: " + doc.get(LuceneConstants.FILE_NAME));
			 l2.add(String.valueOf(scoreDoc.score)); 
			 l1.add(doc.get(LuceneConstants.FILE_NAME));

		 }
		 searcher.close();
	 }
	 public ObservableList getFiles()
	 {
		 return l1;
	 }

	 public ObservableList getHits()
	 {
		 return l2;
	 }

	 public String getKeyWord()
	 {
		 return keyword;
	 }

	 public String getKeyWord2()
	 {
		 return keyword2;
	 }

	 public String getField()
	 {
		 return Sfield;
	 }

	 public String getField2()
	 {
		 return Sfield2;
	 }
	 public void DeleteIndexFiles() throws IOException,ParseException
	 {
		  
		 Path indexPath = Paths.get(indexDir);
		
		//We delete the files inside the folder "Index"
		 Directory indexDirectory = FSDirectory.open(indexPath);
		 IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
		 IndexWriter writer = new IndexWriter(indexDirectory, config);
		 writer.deleteAll();
		 writer.commit();
		 writer.close();
		 
	 }

	 public void AddIndex(File file) throws IOException
	 {
		 indexer.indexFile(file);
	 }
}

