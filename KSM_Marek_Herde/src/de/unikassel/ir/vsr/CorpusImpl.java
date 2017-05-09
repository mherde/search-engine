package de.unikassel.ir.vsr;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class CorpusImpl implements Corpus {

	/**
	 * contains all documents
	 */
	private HashSet<Document> corpus = new HashSet<>();
	/**
	 * number of document in corpus
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
		Collection<Document> result = new LinkedList<>();

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

			/* if document matchs -> adding document to result */
			if (match) {
				result.add(doc);
			}
		}
		
		return result;
	}

	@Override
	public Collection<Document> getDocumentsContainingAny(String... terms) {
		/* saves the documents that contains at least one given term */
		Collection<Document> result = new LinkedList<>();
		
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
			
			/* if document matchs -> adding document to result */
			if (match) {
				result.add(doc);
			}
		}
		
		return result;
	}

	@Override
	public int size() {
		
		/* return number of docments in this corpus */
		return this.size;
	}

}