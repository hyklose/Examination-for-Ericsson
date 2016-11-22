package com.heye.modules;

import java.io.IOException;
import java.util.Collections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.heye.common.BookInfo;
import com.heye.manager.BookList;
import com.heye.manager.UrlQueue;

public class HttpAnalyzer {
	public static int index = 0;
	
	Document httpdocument = null;
	BookInfo bookinfo = null;
	
	public boolean analyze( String Url ) {
		
		boolean result = true;
		
		if( !Url.contains( "/subject/" ) || Url.contains( "icn" ) || Url.contains( "#" ) ) {
			index = 10;
			return result;
		}
		String url = Url.substring( 0, 39);
		
		if( Collections.frequency( UrlQueue.BookUrl, url ) >= 0 ) {
			index = 10;
			return result;
		}
		
		boolean condition = false;
		BookList booklist = new BookList();
		
		index++;
		
		try {
			httpdocument = this.getHttpDocument( url );
			bookinfo = this.getBookInfo( httpdocument );
			condition = this.checkBookCondition( bookinfo );
			
			if( condition == true ) {
				BookList.Mutex.lock();
				booklist.AddNewBook( bookinfo );
				UrlQueue.BookUrl.add( url );
				BookList.Mutex.unlock();
			}
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = false;
		}
		catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
	private Document getHttpDocument( String url ) throws IOException {
		Document document = Jsoup.connect(url).post();
		return document;
	}
	
	private BookInfo getBookInfo( Document document ) {
		BookInfo bookinfo = new BookInfo();
		String allhtml = document.text();
		
		String markdocs  = allhtml.substring( allhtml.indexOf( "豆瓣评分" ), allhtml.indexOf( " 评价:" ) ).replace( "豆瓣评分", "" );
		String[] markdoc = markdocs.split( " " );
		
		bookinfo.Name		= allhtml.substring( allhtml.indexOf("纸书 "		),allhtml.indexOf("作者:") 		).replace( "纸书 ", "" );
		bookinfo.Writer		= allhtml.substring( allhtml.indexOf("作者: "	),allhtml.indexOf("出版社: ") 	).replace( "作者: ", "" );
		if( allhtml.contains( "原作名: " ) ) {
			bookinfo.Publicing	= allhtml.substring( allhtml.indexOf("出版社: "	),allhtml.indexOf("原作名: ") 	).replace( "出版社: ", "" );
		}
		else if( allhtml.contains( "译者:" ) ) {
			bookinfo.Publicing	= allhtml.substring( allhtml.indexOf("出版社: "	),allhtml.indexOf("译者: ") 	).replace( "出版社: ", "" );
		}
		else if( allhtml.contains( "出版年:" ) ) {
			bookinfo.Publicing	= allhtml.substring( allhtml.indexOf("出版社: "	),allhtml.indexOf("出版年: ") 	).replace( "出版社: ", "" );
		}
		bookinfo.Mark		= markdoc[1];
		bookinfo.Evaluate	= markdoc[2];
		bookinfo.Introduce	= allhtml.substring( allhtml.indexOf("内容简介"	),allhtml.indexOf("作者简介") 	).replace( "内容简介: ", "" );
		
		return bookinfo;
	}
	
	private boolean checkBookCondition( BookInfo bookinfo ) {
		if( Integer.valueOf( bookinfo.Evaluate.replace( "人评价", "" ) ) < 2000 ) {
			return false;
		}
		
		if( bookinfo.Name.contains( "互联网" ) || bookinfo.Introduce.contains( "互联网" ) ) {
			return true;
		}
		else if( bookinfo.Name.contains( "编程" ) || bookinfo.Introduce.contains( "编程" ) ) {
			return true;
		}
		else if( bookinfo.Name.contains( "算法" ) || bookinfo.Introduce.contains( "算法" ) ) {
			return true;
		}
		
		return false;
	}
}
