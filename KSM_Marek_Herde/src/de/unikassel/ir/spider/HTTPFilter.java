package de.unikassel.ir.spider;

import java.net.URL;

public class HTTPFilter implements URLFilter {
	
	private boolean testProtocol(URL url){
		return url.getProtocol().equals("http") || url.getProtocol().equals("https");
	}

	@Override
	public boolean shouldInclude(URL from, URL to) {
		/*
		 * testing protocol of urls, if from is null (only at start of crawler)
		 * set , otherwise test http protocol
		 */
		boolean fromPage = (from == null) ? true : this.testProtocol(from);
		/*
		 * testing protocol of urls, if to is null set false, otherwise test
		 * http protocol
		 */
		boolean toPage = (to == null) ? false : this.testProtocol(to);

		/* both URLs must have http as protocol */
		return fromPage && toPage;
	}

}
