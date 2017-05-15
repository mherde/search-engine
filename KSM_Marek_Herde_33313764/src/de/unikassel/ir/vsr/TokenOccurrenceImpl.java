package de.unikassel.ir.vsr;

import java.util.List;

public class TokenOccurrenceImpl implements TokenOccurrence {

	private Document docRef;
	private String token;

	public TokenOccurrenceImpl(Document docRef, String token) {
		this.docRef = docRef;
		this.token = token;
	}

	@Override
	public int compareTo(TokenOccurrence o) {

		if (this.getWeight() > o.getWeight()) {
			/* this token occurrence has a greater weight */
			return 1;
		} else if (this.getWeight() < o.getWeight()) {
			/* this token occurrence has a smaller weight */
			return -1;
		} else {
			/* weights of both token occurrences are equal */
			return 0;
		}
		
	}

	@Override
	public Document getDocument() {
		return this.docRef;
	}

	@Override
	public double getWeight() {
		return this.docRef.getTermCount(this.token);
	}

	@Override
	public List<Integer> getPositions() {
		return this.docRef.getTermPositions(this.token);
	}

}