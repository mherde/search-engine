package de.unikassel.ir.spider;

import java.net.URL;

public class HTTPFilter implements URLFilter {

	@Override
	public boolean shouldInclude(URL from, URL to) {
		boolean fromPage;
		if (from != null)
			fromPage = from.getProtocol().equals("https") || from.getProtocol().equals("http");
		else
			fromPage = true;
		
		boolean toPage = to.getProtocol().equals("https") || from.getProtocol().equals("http");
		
		return fromPage && toPage;
	}

}