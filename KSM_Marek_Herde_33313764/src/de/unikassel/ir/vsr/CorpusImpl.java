package de.unikassel.ir.vsr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CorpusImpl implements Corpus {

	/**
	 * contains all documents
	 */
	private ArrayList<Document> corpus = new ArrayList<>();
	/**
	 * number of documents in corpus
	 */
	private int size;

	@Override
	public void addDocument(Document doc) {

		/* adding document */
		corpus.add(doc);

		/* increasing number of documents */
		this.size++;
	}

	@Override
	public Iterator<Document> iterator() {

		/* returning iterator on the corpus */
		return corpus.iterator();
	}

	@Override
	public Collection<Document> getDocumentsContainingAll(String... terms) {
		/* saves the documents that contains all given terms */
		Collection<Document> result = new ArrayList<>();

		/* iteration over all documents */
		Iterator<Document> iterator = this.iterator();
		while (iterator.hasNext()) {
			Document doc = iterator.next();
			boolean match = true;

			/*
			 * if a the current document does not contain a given term, it does
			 * not match the query -> break further investigation of this
			 * document
			 */
			for (String term : terms) {
				if (doc.getTermCount(term) == 0) {
					match = false;
					break;
				}
			}

			/* if document matches query -> adding document to result */
			if (match) {
				result.add(doc);
			}
		}
		
		return result;
	}

	@Override
	public Collection<Document> getDocumentsContainingAny(String... terms) {
		/* saves the documents that contains at least one given term */
		Collection<Document> result = new ArrayList<>();
		
		/* iteration over all documents */
		Iterator<Document> iterator = this.iterator();
		while (iterator.hasNext()) {
			Document doc = iterator.next();
			boolean match = false;
			
			/*
			 * if a the current document does contain a given term, it does
			 * match the query -> break further investigation of this
			 * document
			 */
			for (String term : terms) {
				if (doc.getTermCount(term) > 0) {
					match = true;
					break;
				}
			}
			
			/* if document matches query -> adding document to result */
			if (match) {
				result.add(doc);
			}
		}
		
		return result;
	}

	@Override
	public int size() {
		
		/* return number of documents in this corpus */
		return this.size;
	}

}
