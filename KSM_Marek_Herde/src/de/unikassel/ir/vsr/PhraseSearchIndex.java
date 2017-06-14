package de.unikassel.ir.vsr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PhraseSearchIndex extends InvertedIndexImpl implements PhraseSearch {

	@Override
	public Map<Document, List<Integer>> searchPhrase(List<String> phrase) {

		/* documents containing phrase */
		Map<Document, List<Integer>> result = new HashMap<>();

		/* ensuring phrase not to be null and phrase has at least one element */
		if (phrase == null || phrase.isEmpty())
			return result;

		/*
		 * creating list for every term in phrase, lists are sorted after
		 * document and position
		 */
		List<List<Pair>> lists = this.createSortedLists(phrase);

		/* if no possible phrase was found -> return */
		if (lists.isEmpty()) {
			return result;
		}

		/* pointers as index referencing pair in a term's list */
		int[] pointers = new int[phrase.size()];
		Arrays.fill(pointers, 0);

		/* itertation over all possible positions of the phrase's first term */
		while (pointers[0] < lists.get(0).size()) {

			/* match as base */
			boolean match = true;

			/*
			 * iteration over the all terms apart from the first in the phrase
			 */
			for (int j = 1; j < pointers.length; j++) {

				/*
				 * pairs of the j-1./j. term's list at index pointers[j -
				 * 1]/pointers[j]
				 */
				Pair pair_j_1 = lists.get(j - 1).get(pointers[j - 1]);
				Pair pair_j = lists.get(j).get(pointers[j]);

				/* document id's of the above pairs */
				String dk_j_1 = pair_j_1.getDocument().getId().toString();
				String dk_j = pair_j.getDocument().getId().toString();

				/* positions of the above pairs */
				int pk_j_1 = pair_j_1.getPosition();
				int pk_j = pair_j.getPosition();

				/*
				 * while there is no match between the documents or the current
				 * document of the j. term has lower order than the current
				 * document of j-1. terms, look for next pair of position and
				 * document in j. term list
				 */
				while (!(dk_j.compareTo(dk_j_1) > 0 || (dk_j.equals(dk_j_1) && pk_j >= pk_j_1 + 1))) {
					/* if we have not reached end of j. term list */
					if (pointers[j] < lists.get(j).size() - 1) {
						/* increasing pointer of j. term */
						pointers[j]++;
						/*
						 * setting new values for pair and corresponding
						 * document as well as position
						 */
						pair_j = lists.get(j).get(pointers[j]);
						dk_j = pair_j.getDocument().getId().toString();
						pk_j = pair_j.getPosition();
					} else {
						/*
						 * if the document of the j-1. token has a higher order
						 * and we have already reached the end of the list of
						 * the j. token, we can return result, because there
						 * can't be a further match
						 */
						return result;
					}
				}

				/*
				 * documents are different or the positions are not successive
				 * -> no match
				 */
				if (!dk_j.equals(dk_j_1) || pk_j != pk_j_1 + 1) {
					match = false;
					break;
				}
			}

			/*
			 * no mismatch -> add found document with first position of phrase
			 */
			if (match) {
				/* get pair of first term */
				Pair resultPair = lists.get(0).get(pointers[0]);

				/*
				 * adding new position and add document with updated positions
				 * to result
				 */
				List<Integer> positions = result.getOrDefault(resultPair.getDocument(), new ArrayList<Integer>());
				positions.add(resultPair.getPosition());
				result.put(resultPair.getDocument(), positions);
			}

			/* go for next entry of first term's list */
			pointers[0]++;
		}

		return result;

	}

	/**
	 * Creating a list of lists of pairs sorted by document id and position
	 * 
	 * @param phrase
	 *            determine number of lists and content of these lists
	 * @return list of lists sorted after document id and position of a pair
	 */
	private List<List<Pair>> createSortedLists(List<String> phrase) {

		/* list that contains all sorted lists */
		List<List<Pair>> lists = new ArrayList<>();

		/* for every token in phrase create list */
		for (String token : phrase) {

			/* ignoring capitalization */
			token = token.toLowerCase();

			/*
			 * if invertedIndex does not contain that token, phrase is not in
			 * corpus
			 */
			if (tokenHash.get(token) == null) {
				return new ArrayList<>();
			}

			/*
			 * contains the pairs of document and position regarding the current
			 * token
			 */
			List<Pair> sortedList = new ArrayList<>();

			/* filling sortedList with these pairs of document and position */
			for (TokenOccurrence occ : tokenHash.get(token).getTokenOccurrenceList()) {
				/*
				 * adding for every position in document a pair of this document
				 * and position to list
				 */
				for (int position : occ.getPositions()) {
					sortedList.add(new Pair(occ.getDocument(), position));
				}
			}

			/* sorting list regarding comparedTo() in class Pair */
			Collections.sort(sortedList);

			lists.add(sortedList);
		}

		return lists;

	}

	/**
	 * Class representing datastructure to store document and position of a
	 * term.
	 * 
	 * @author marekherde
	 *
	 */
	private class Pair implements Comparable<Pair> {

		private Document doc;
		private int position;

		public Pair(Document doc, int position) {
			this.doc = doc;
			this.position = position;
		}

		public Document getDocument() {
			return this.doc;
		}

		public int getPosition() {
			return this.position;
		}

		@Override
		public int compareTo(Pair o) {
			/* comparison of document ids */
			int comp = this.getDocument().getId().compareTo(o.getDocument().getId());

			/* not equal -> ascending lexicographically order */
			if (comp != 0) {
				return comp;
			} else {
				/* ascending order of positions */
				return this.getPosition() > o.getPosition() ? +1 : this.getPosition() < o.getPosition() ? -1 : 0;
			}
		}

		@Override
		public String toString() {
			return "(" + this.getDocument().getId() + ", " + this.getPosition() + ")";
		}

	}

	/**
	 * Constructor based on super constructor of InvertedIndexImpl
	 * 
	 * @param corpus
	 */
	public PhraseSearchIndex(Corpus corpus) {
		super(corpus);
	}

	@Override
	public Map<Document, List<Integer>> searchPhrase(String phrase) {

		/* splitting phrase regarding blank */
		String[] terms = phrase.trim().split("\\s+");

		/* call searchPhrase() with list of terms */
		return this.searchPhrase(Arrays.asList(terms));

	}

	@Override
	public List<String> getContext(String phrase, Document doc, int pos) {
		/* splitting phrase in terms */
		String[] terms = phrase.trim().split("\\s+");

		/* cast to get term index of document */
		DocumentImpl document = (DocumentImpl) doc;
		Map<String, ArrayList<Integer>> termsIndex = document.getTermsIndex();

		/* start positions before and after phrase */
		int beforeStartPosition = pos - 1;
		int afterStartPosition = pos + terms.length;
		

		/* number of terms that belongs to context */
		int currentNumberOfTerms = 0;

		/*
		 * number of terms that can are before or after phrase, maximal 5 terms
		 * before and 5 terms after are regarded
		 */
		int beforeNumberOfTerms = Math.min(pos, 5);
		int afterNumberOfTerms = Math.min(doc.size() - afterStartPosition, 5);
		

		/* term number of context, can be maximal 5 + 5 = 10 */
		int maxNumberOfTerms = beforeNumberOfTerms + afterNumberOfTerms;

		/* arrays to store terms in correct order */
		String[] beforeArray = new String[beforeNumberOfTerms];
		String[] afterArray = new String[afterNumberOfTerms];

		/* maximal length of the both arrays */
		int maxLength = Math.max(beforeArray.length, afterArray.length);

		/* iteration over all terms in documents */
		for (Entry<String, ArrayList<Integer>> entry : termsIndex.entrySet()) {
			int posBefore = beforeStartPosition;
			int posAfter = afterStartPosition;

			/*
			 * checking whether one of the searched positions is in the positions
			 * list of the current term
			 */
			for (int i = 0; i < maxLength; i++) {
				if (entry.getValue().contains(posBefore)) {
					beforeArray[posBefore - pos + beforeArray.length] = entry.getKey();
					currentNumberOfTerms++;
				}

				if (entry.getValue().contains(posAfter)) {
					afterArray[posAfter - pos - terms.length] = entry.getKey();
					currentNumberOfTerms++;
				}

				/* test next position */
				posBefore--;
				posAfter++;
			}

			/* all terms before and after phrase were found -> break */
			if (currentNumberOfTerms > maxNumberOfTerms) {
				break;
			}
		}

		/* creation of StringBuilders containing before and after term context */
		StringBuilder before = new StringBuilder();
		StringBuilder after = new StringBuilder();

		/* filling of StringBuilders with found terms */
		for (int i = 0; i < maxLength; i++) {
			if (i < beforeArray.length) {
				before.append(beforeArray[i]);
				if (i < beforeArray.length - 1)
					before.append(" ");
			}
			if (i < afterArray.length) {
				after.append(afterArray[i]);
				if (i < afterArray.length - 1)
					after.append(" ");
			}
		}

		/* creating and filling of context */
		List<String> context = new ArrayList<>();
		context.add(before.toString());
		context.add(after.toString());

		return context;
	}

}