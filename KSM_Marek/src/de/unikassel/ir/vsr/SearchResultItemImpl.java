package de.unikassel.ir.vsr;

public class SearchResultItemImpl implements SearchResultItem {

	/** reference to the found document */
	private Document document;

	/** cosines similarity w.r.t. query */
	private double similarityScore;

	public SearchResultItemImpl(Document document, double similarityScore) {
		this.document = document;
		this.similarityScore = similarityScore;
	}

	@Override
	public int compareTo(SearchResultItem o) {

		if (this.equals(o)) {
			/*
			 * the search results have an equal similarity score, because they
			 * are identical objects regarding the equal method
			 */
			return 0;
		} else if (this.getSimilarityScore() > o.getSimilarityScore()) {
			/* this search result has a greater similarity score */
			return -1;
		} else {
			/* this search result has a smaller or equal similarity score */
			return 1;
		}

	}

	@Override
	public Document getDocument() {
		return this.document;
	}

	@Override
	public double getSimilarityScore() {
		return this.similarityScore;
	}

	@Override
	public void setSimilarityScore(double similarityScore) {
		this.similarityScore = similarityScore;
	}

	@Override
	public String toString() {
		return "(" + this.document.getId() + ", " + this.similarityScore + ")";
	}

}
