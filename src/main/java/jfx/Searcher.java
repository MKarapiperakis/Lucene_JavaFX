package jfx;



import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;



import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;


public class Searcher {
	 IndexSearcher indexSearcher;
	 Directory indexDirectory;
	 IndexReader indexReader;
	 QueryParser queryParser;
	
	 Query query;
	 Query query2;

	 public Searcher(String indexDirectoryPath,String field) throws IOException
	 {
		 
		 Path indexPath = Paths.get(indexDirectoryPath);	//indexPath is the path where our folder is
		 indexDirectory = FSDirectory.open(indexPath);
		 indexReader = DirectoryReader.open(indexDirectory);
		 indexSearcher = new IndexSearcher(indexReader);
		 if(field == "body")
		 	queryParser = new QueryParser(LuceneConstants.BODY, new StandardAnalyzer());	//the constant here shows us the field that we are searching for.
		 else if(field == "title") 
		 	queryParser = new QueryParser(LuceneConstants.TITLE,new StandardAnalyzer());
		 else if(field == "people")
		 	queryParser = new QueryParser(LuceneConstants.PEOPLE, new StandardAnalyzer());
		 else if(field == "places")
		 	queryParser = new QueryParser(LuceneConstants.PLACES, new StandardAnalyzer());

			
	 }

	 public TopDocs search(String searchQuery) throws IOException,ParseException 
	 {

		 query = queryParser.parse(searchQuery);
		 //System.out.println(query);
		 //System.out.println("query: "+ query.toString());
		 return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	 }

	 public TopDocs search(Builder query)throws IOException, ParseException{
		return indexSearcher.search(query.build(), LuceneConstants.MAX_SEARCH);
	}

	public TopDocs Phsearch(org.apache.lucene.search.PhraseQuery.Builder query3)throws IOException, ParseException{
		return indexSearcher.search(query3.build(), LuceneConstants.MAX_SEARCH);
	}

	
	 public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException 
	 {
		 return indexSearcher.doc(scoreDoc.doc);
	 }
	 
	 public IndexReader getIndexReader()
	 {
		 return indexReader;
	 }
	 public void close() throws IOException 
	 {
		 indexReader.close();
		 indexDirectory.close();
	 }
}
