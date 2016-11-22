package com.heye.common;

import com.heye.manager.UrlQueue;
import com.heye.modules.BookDownloader;

public class Main {
	
	static BookDownloader bookload = new BookDownloader();
	
	private static void PushInitUrl( String url ) {
		UrlQueue.AddUnvisitedUrl( url );
	}
	
	public static void main( String[] args ) {
	
		PushInitUrl( "https://book.douban.com/" );
		
		ProcessThread thread1 = new ProcessThread();
		//ProcessThread thread2 = new ProcessThread();
		//ProcessThread thread3 = new ProcessThread();
		//ProcessThread thread4 = new ProcessThread();
		thread1.start();
		//thread2.start();
		//thread3.start();
		//thread4.start();
		
		try {
			thread1.join();
			//thread2.join();
			//thread3.join();
			//thread4.join();
			
			bookload.DownloadtoExcel();
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			
		}
		return;
	}

}
