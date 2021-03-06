package de.unikassel.ir.vsr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CorpusImpl implements Corpus {

	/**
	 * contains all documents
	 */
	private List<Document> allDocuments = new ArrayList<Document>();
	/**
	 * mapping between term and documents, containing the mapped term
	 */
	private HashMap<String, List<Document>> corpus = new HashMap<>();
	/**
	 * number of documents in corpus
	 */
	private int size;

	@Override
	public void addDocument(Document doc) {

		/* adding document */
		allDocuments.add(doc);

		/* creating of mapping between term and its documents */
		for (String term : doc) {
			if (corpus.containsKey(term)) {
				List<Document> documentList = corpus.get(term);
				documentList.add(doc);
			} else {
				List<Document> documentList = new ArrayList<Document>();
				documentList.add(doc);
				corpus.put(term, documentList);

			}
		}

		/* increasing number of documents */
		this.size++;
	}

	@Override
	public Iterator<Document> iterator() {

		/* returning iterator on the corpus */
		return this.allDocuments.iterator();
	}

	@Override
	public Collection<Document> getDocumentsContainingAll(String... terms) {
		/* saves the documents that contains all given terms */
		Collection<Document> result = new HashSet<>();

		/* generating intersection of the query terms' documents list */
		for (String term : terms) {
			term = term.toLowerCase();
			/* initial adding of a document list */
			if (result.size() == 0) {
				List<Document> docs = this.corpus.get(term);
				if (docs != null)
					result.addAll(docs);
			} else {
				/*
				 * intersection of current result with the document list of
				 * current term
				 */
				List<Document> docs = this.corpus.get(term);
				if (docs != null)
					result.retainAll(this.corpus.get(docs));
			}
		}

		return result;
	}

	@Override
	public Collection<Document> getDocumentsContainingAny(String... terms) {
		/* saves the documents that contains at least one given term */
		Collection<Document> result = new ArrayList<>();

		/* generating set union of query terms' document lists */
		for (String term : terms) {
			term = term.toLowerCase();
			List<Document> docs = this.corpus.get(term);
			if (docs != null)
				result.addAll(docs);
		}

		return result;
	}

	@Override
	public int size() {

		/* return number of documents in this corpus */
		return this.size;
	}

}
