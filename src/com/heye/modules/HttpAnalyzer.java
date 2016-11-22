package com.heye.modules;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.heye.common.BookInfo;
import com.heye.manager.BookList;

public class HttpAnalyzer {
	Document httpdocument = null;
	BookInfo bookinfo = null;
	
	public boolean analyze( String Url ) {
		
		boolean result = true;
		
		if( !Url.contains( "/subject/" ) || Url.contains( "icn" ) || Url.contains( "#" ) ) {
			return result;
		}
		String url = Url.substring( 0, 40);
		
		boolean condition = false;
		BookList booklist = new BookList();
		
		try {
			httpdocument = this.getHttpDocument( url );
			bookinfo = this.getBookInfo( httpdocument );
			condition = this.checkBookCondition( bookinfo );
			
			if( condition == true ) {
				BookList.Mutex.lock();
				booklist.AddNewBook( bookinfo );
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
		
		String markdocs  = allhtml.substring( allhtml.indexOf( "��������" ), allhtml.indexOf( " ����:" ) ).replace( "��������", "" );
		String[] markdoc = markdocs.split( " " );
		
		bookinfo.Name		= allhtml.substring( allhtml.indexOf("ֽ�� "		),allhtml.indexOf("����:") 		).replace( "ֽ�� ", "" );
		bookinfo.Writer		= allhtml.substring( allhtml.indexOf("����: "	),allhtml.indexOf("������: ") 	).replace( "����: ", "" );
		if( allhtml.contains( "ԭ����: " ) ) {
			bookinfo.Publicing	= allhtml.substring( allhtml.indexOf("������: "	),allhtml.indexOf("ԭ����: ") 	).replace( "������: ", "" );
		}
		else if( allhtml.contains( "����:" ) ) {
			bookinfo.Publicing	= allhtml.substring( allhtml.indexOf("������: "	),allhtml.indexOf("����: ") 	).replace( "������: ", "" );
		}
		else if( allhtml.contains( "������:" ) ) {
			bookinfo.Publicing	= allhtml.substring( allhtml.indexOf("������: "	),allhtml.indexOf("������: ") 	).replace( "������: ", "" );
		}
		bookinfo.Mark		= markdoc[1];
		bookinfo.Evaluate	= markdoc[2];
		bookinfo.Introduce	= allhtml.substring( allhtml.indexOf("���ݼ��"	),allhtml.indexOf("���߼��") 	).replace( "���ݼ��: ", "" );
		
		return bookinfo;
	}
	
	private boolean checkBookCondition( BookInfo bookinfo ) {
		if( Integer.valueOf( bookinfo.Evaluate.replace( "������", "" ) ) < 2000 ) {
			return false;
		}
		
		if( bookinfo.Name.contains( "������" ) || bookinfo.Introduce.contains( "������" ) ) {
			return true;
		}
		else if( bookinfo.Name.contains( "���" ) || bookinfo.Introduce.contains( "���" ) ) {
			return true;
		}
		else if( bookinfo.Name.contains( "�㷨" ) || bookinfo.Introduce.contains( "�㷨" ) ) {
			return true;
		}
		
		return false;
	}
}
