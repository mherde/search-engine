package de.unikassel.ir.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.unikassel.ir.vsr.Corpus;
import de.unikassel.ir.vsr.CorpusImpl;
//import de.unikassel.ir.vsr.CorpusImpl;
import de.unikassel.ir.vsr.Document;
//import de.unikassel.ir.vsr.DocumentImpl;
import de.unikassel.ir.vsr.DocumentImpl;
import de.unikassel.ir.vsr.InvertedIndex;
import de.unikassel.ir.vsr.InvertedIndexImpl;
import de.unikassel.ir.vsr.SearchResultItem;

/**
 * Search Engine Bean to use the query methods in JSPs
 * @author Beate Krause
 */
public class SearchEngine {
	
	/**
	 * The corpus
	 */
	private Corpus corpus;
	/**
	 * inverted index
	 */
	private InvertedIndex index;

	/**
	 * Constructor, sets the corpus
	 */
	public SearchEngine() {
		corpus = new CorpusImpl();
		loadDefaultCorpus();
		index = new InvertedIndexImpl(corpus);
	}
	
	/**
	 * Load Texts into the corpus
	 */
	public void loadDefaultCorpus() {
		
		//TODO: set path for corpus
		File dir = new File(MyServlet.corpusPath);

		for (File file : dir.listFiles()) {
			if (!file.isDirectory()) {
				FileInputStream stream;
				try {
					stream = new FileInputStream(file);
					Document doc = new DocumentImpl(file.getName());
					doc.read(stream);
					stream.close();
					corpus.addDocument(doc);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Query for terms in the corpus
	 * @param terms query
	 * @param andOperator AND or OR query
	 * @return set of document ids
	 */
	public List<String> testQuery(String terms, boolean andOperator) {
		
		String[] termsRequest = terms.split("\\s+");
		
		
		List<String> resultIDs = new ArrayList<String>();
		Collection<Document> docs;
		
		if (andOperator){
			docs = corpus.getDocumentsContainingAll(termsRequest);
		}else{
			docs = corpus.getDocumentsContainingAny(termsRequest);
		}
		
		for (Document doc : docs) {
			resultIDs.add(doc.getId());
		}
		
		return resultIDs;
	}
	
	/**
	 * calculates list of documents ranked regarding the cosines similarity w.r.t. query
	 * @param terms
	 * @return ranked list of documents
	 */
	public List<String> testRankedQuery(String terms){
		/* splitting query into terms */
		String[] query = terms.split("\\s+");
		
		/* stores result list of documents */
		List<String> results = new ArrayList<String>();

		/* iterator over all found documents ordered by their cosines similarity */
		Iterator<? extends SearchResultItem> searchResults = index.getCosineSimilarities(query);
		
		/* adding found docuements to result list */
		int i = 1;
		while(searchResults.hasNext()){
			SearchResultItem item = searchResults.next();
			results.add(i+". "+item.toString());
			i++;
		}
		
		return results;

	}
	
	/**
	 * @return the Corpus
	 */
	public Corpus getCorpus() {
		return corpus;
	}
}