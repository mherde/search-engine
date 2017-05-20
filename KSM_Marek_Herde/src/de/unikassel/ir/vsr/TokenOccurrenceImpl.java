package de.unikassel.ir.vsr;

import java.util.List;

public class TokenOccurrenceImpl implements TokenOccurrence {

	/** reference to the document containing the token */
	private Document docRef;

	/** token info to corresponding token */
	private TokenInfo tokenInfo;

	/** term frequency in the document */
	private double documentTF;

	/** list of positions where the token occurs */
	private List<Integer> positions;
	
	/** tf-idf */

	/**
	 * constructor to initialize all important values
	 * 
	 * @param docRef
	 * @param tokenInfo
	 * @param token
	 */
	public TokenOccurrenceImpl(Document docRef, TokenInfo tokenInfo, String token) {
		this.docRef = docRef;
		this.tokenInfo = tokenInfo;
		this.documentTF = this.docRef.getTF(token);
		this.positions = this.docRef.getTermPositions(token);
	}

	@Override
	public int compareTo(TokenOccurrence o) {

		if (this.getWeight() > o.getWeight()) {
			/* this token occurrence has a greater weight */
			return -1;
		} else if (this.getWeight() < o.getWeight()) {
			/* this token occurrence has a smaller weight */
			return 1;
		} else if (this.equals(o)) {
			/* weights of both token occurrences are equal */
			return 0;
		}

		return 1;

	}

	@Override
	public Document getDocument() {
		return this.docRef;
	}

	@Override
	public double getWeight() {
		/* calculation of tf-idf */
		return this.tokenInfo.getIdf() * this.documentTF;
	}

	@Override
	public List<Integer> getPositions() {
		return this.positions;
	}

	@Override
	public String toString() {
		return this.docRef.getId() + " -> " + this.getWeight();
	}

}
