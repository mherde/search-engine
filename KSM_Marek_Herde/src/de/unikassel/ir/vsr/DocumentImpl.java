package de.unikassel.ir.vsr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DocumentImpl implements Document {

	/**
	 * represents id of this document
	 */
	private String id;
	/**
	 * mapping between term and an list of position where this term occurs
	 */
	protected HashMap<String, ArrayList<Integer>> termsIndex;
	/**
	 * list containing all terms in their order of occurence
	 */
	protected List<String> allTerms;
	/**
	 * represents size or rather number of words in this document
	 */
	protected int size = 0;
	/**
	 * represents the maximal frequency of term in this document
	 */
	protected int maximalFrequency = 1;
	/**
	 * private double length of document vector
	 */
	protected double docLength = 0.;

	/**
	 * constructor initializing document id
	 */
	public DocumentImpl(String fileName) {
		this.id = fileName;
	}
	
	public DocumentImpl() {
	}

	@Override
	public Iterator<String> iterator() {

		/* simply return an iterator on the keys of the map */
		return this.termsIndex.keySet().iterator();
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void read(InputStream input) throws IOException {

		/* represents the current position of scanned words */
		int currentPosition = 0;

		/* reader to read document */
		BufferedReader in = new BufferedReader(new InputStreamReader(input));

		/* stores all lines of the document */
		ArrayList<String[]> lines = new ArrayList<>();

		/* stores currentLine */
		String currentLine;

		/* reading all lines of document */
		while ((currentLine = in.readLine()) != null) {

			/* adding line as array of terms */
			lines.add(currentLine.trim().split("\\s+"));
		}

		/* initialization of the map */
		this.termsIndex = new HashMap<>();
		this.allTerms = new ArrayList<>();

		/* iteration over all lines of the document */
		for (String[] termsOfOneLine : lines) {

			/* iteration over all terms of the current lime */
			for (String term : termsOfOneLine) {
				
				this.allTerms.add(term);

				/* if word is not listed in map */
				if (!this.termsIndex.containsKey(term)) {

					/* create an entry with a corresponding arraylist */
					ArrayList<Integer> positions = new ArrayList<>();

					/* adding position where the current word occurs */
					positions.add(currentPosition);

					this.termsIndex.put(term, positions);
				} else {

					/* getting corresponding position list */
					ArrayList<Integer> positions = this.termsIndex.get(term);

					/* adding position where the current word occurs */
					positions.add(currentPosition);
				}

				/* increasing current position */
				currentPosition++;
				/* increasing number of scanned words */
				this.size++;
			}
		}

		determineMaximalFrequency();

	}

	/**
	 * calculation of maximal frequency of a term in the document
	 */
	protected void determineMaximalFrequency() {
		/* iteration over all terms */
		for (String term : this.termsIndex.keySet()) {

			int termCount = this.getTermCount(term);

			/*
			 * if current term frequency is bigger than past maximal frequency
			 */
			if (termCount > this.maximalFrequency) {

				/* setting new maximal frequency */
				this.maximalFrequency = termCount;
			}
		}
	}

	@Override
	public int getTermCount(String term) {

		/* if this document contains the term */
		if (this.termsIndex.containsKey(term)) {

			/*
			 * calculation of number of occurrences by getting number of
			 * positions
			 */
			return this.termsIndex.get(term).size();

			/* if this document does not contain the term */
		} else {

			/* number of occurrences is 0 */
			return 0;
		}
	}

	@Override
	public ArrayList<Integer> getTermPositions(String term) {

		/*
		 * returning list of positions by searching for the corresponding map
		 * entry
		 */
		return this.termsIndex.get(term);
	}

	@Override
	public int size() {

		/* return number of words in this document */
		return this.size;
	}

	/**
	 * Calculation of normalized term frequency of term by normalizing with
	 * maximal frequency of a term in this document
	 * 
	 * @return normalized term frequency of term
	 */
	public double getTF(String term) {
		double termCount = this.getTermCount(term);

		/* normalizing term frequency */
		return termCount / this.maximalFrequency;
	}
	
	@Override
	public String toString(){
		String toString = "";
		for(String term : this.termsIndex.keySet()){
			toString += term+"->positions: "+this.termsIndex.get(term)+" \n";
		}
		return toString;
	}

	/**
	 * @return length of document vector
	 */
	public double getDocLength() {
		return this.docLength;
	}

	/**
	 * Setting new value of document vector length
	 * @param docLenght: new value for the length of the document vector
	 */
	public void setDocLength(double docLength) {
		this.docLength = docLength;
	}
	
	public HashMap<String, ArrayList<Integer>> getTermsIndex(){
		return this.termsIndex;
	}

}
