package de.unikassel.ir.vsr;

import java.util.TreeSet;

public class TokenInfoImpl implements TokenInfo {

	/** list of all occurrences of the corresponding token */
	private TreeSet<TokenOccurrence> occList;

	/** idf of the corresponding token */
	double idf = 1;

	public TokenInfoImpl() {
		this.occList = new TreeSet<>();
	}

	@Override
	public void setIdf(double idf) {
		this.idf = idf;
	}

	@Override
	public double getIdf() {
		return this.idf;
	}

	@Override
	public TreeSet<TokenOccurrence> getTokenOccurrenceList() {
		return this.occList;
	}

}
