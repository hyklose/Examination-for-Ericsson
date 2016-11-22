package com.heye.common;

import java.util.Collections;
import java.util.Set;

import com.heye.manager.UrlQueue;
import com.heye.modules.HttpAnalyzer;
import com.heye.modules.UrlSpider;

public class ProcessThread extends Thread {

	public void run() {
		LinkFilter filter = new LinkFilter() {
			public boolean accept(String url) {
				if (url.startsWith("https://book.douban.com/subject/"))
					return true;
				else
					return false;
			}
		};
		HttpAnalyzer analyzer = new HttpAnalyzer();
		
		while( true ) {
			UrlQueue.Mutex.lock();
			if( UrlQueue.UnGradUrlsEmpty() ) {
				UrlQueue.Mutex.unlock();
				break;
			}
			String url = (String) UrlQueue.UnVisitedUrlDeQueue();
			UrlQueue.Mutex.unlock();
			
			if( UrlQueue.BookUrl.size() == 0 ) {
				
			}
			else {
				if( Collections.frequency( UrlQueue.BookUrl, url.substring( 0, 39) ) < 0 ) {
					while( !analyzer.analyze( url ) ) {
					}
					UrlQueue.BookUrl.add( url.substring( 0, 39) );
				}
			}
			UrlQueue.AddVisitedUrl( url );
			Set<String> links = UrlSpider.ExtracLinks( url, filter);
			for (String link : links) {
				UrlQueue.AddUnvisitedUrl( link );
			}
			
			System.out.println( url );
			try {
				Thread.sleep(5);
			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return;
	}
}
