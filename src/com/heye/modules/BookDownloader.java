package com.heye.modules;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.heye.common.BookInfo;
import com.heye.manager.BookList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class BookDownloader {

	public void DownloadtoExcel() {
		
		BookList booklist = new BookList();
		List<BookInfo> infolist = booklist.GetBookList();
		
		try {
			File file = new File( "D:\\BookInfos.xls" );
			
			WritableWorkbook workbook = Workbook.createWorkbook( file );
			WritableSheet sheet = workbook.createSheet("Sheet1", 0);
			
			String[] title = { "书名", "作者", "出版社", "评分", "评价人数", "简介"};
			for( int index = 0; index < 6; ++index ) {
				sheet.addCell( new Label( index, 0, title[index] ) );
			}
			
			if( infolist.size() > 0) {
				for( int index = 0; index < 6; ++index ) {
					String name 		= infolist.get(index).Name;
					String writer 		= infolist.get(index).Writer;
					String publicing 	= infolist.get(index).Publicing;
					String mark 		= infolist.get(index).Mark;
					String evaluate 	= infolist.get(index).Evaluate;
					String introduce 	= infolist.get(index).Introduce;
					
					String[] infodata = { name, writer, publicing, mark, evaluate, introduce };
					for( int index2 = 0; index2 < infolist.size(); ++index2 ) {
						sheet.addCell( new Label( index2, index, infodata[index2] ) );
					}
				}
			}
			
			workbook.write();
			workbook.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			booklist = null;
			infolist = null;
		}
	}
}
