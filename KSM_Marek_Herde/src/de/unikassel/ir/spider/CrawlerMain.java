package de.unikassel.ir.spider;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import de.unikassel.ir.spider.Crawler;
import de.unikassel.ir.vsr.Corpus;
import de.unikassel.ir.vsr.Document;

/**
 * MainProgram: starts a Crawler and presents the corpus
 * 
 * @author Christoph Schmitz
 * @author sdo
 */
public class CrawlerMain {
	static org.apache.log4j.Logger log = Logger.getLogger(CrawlerMain.class);
	
	public static void main(String[] args) {
		BasicConfigurator.configure();		
		try {
			// Number of pages to download
			int nPages = 5;
			// Number of threads to extract the links 
			int nLinkExtractors = 5;			
			// number of HTTP-Queues
			int nWorkers = 10;
			
			// Create a Crawler, Start it and give it a URL to start with
			log.debug("Start...");
			Crawler spider = new Crawler(nPages, nLinkExtractors, nWorkers);
			URL url = new URL("http://www.uni-kassel.de");
			log.debug("Spider initialized\n----------------------------------------------");
			log.debug("Pushing " + url);
			spider.addURL(null, url);			
			
			// Wait until all the nPages have been downloaded and processed
			spider.waitUntilDone();					
			
			// Show Result (Corpus)
			Corpus corpus = spider.getDocumentsAsCorpus();			
			for (Document doc: corpus) {
				log.debug("Doc: " + doc.getId());
			}			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
