package de.unikassel.ir.spider;

import java.net.URL;

public class SameHostURLFilter implements URLFilter {

	@Override
	public boolean shouldInclude(URL from, URL to) {
		if (from != null)
			return from.getHost().equals(to.getHost());
		else
			return true;

	}

}