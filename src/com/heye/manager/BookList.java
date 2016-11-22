package com.heye.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.heye.common.BookInfo;

public class BookList {
	
	private static List<BookInfo> booklist = new ArrayList<BookInfo>();
	
	public static Lock Mutex = new ReentrantLock();
	
	public void AddNewBook( BookInfo bookinfo ) {
		booklist.add( bookinfo );
		return;
	}
	
	public int GetEnableNum() {
		return booklist.size();
	}
	
	public List<BookInfo> GetBookList() {
		return booklist;
	}
	
	public boolean IsEmplty() {
		return booklist.isEmpty();
	}
	
	public void SortBookInfo() {
		Collections.sort( booklist, new SortCondition() );
	}

	class SortCondition implements Comparator<Object> {

		@Override
		public int compare(Object arg0, Object arg1) {
			BookInfo bookinfo = new BookInfo();
			
			if( Double.valueOf(((BookInfo)arg0).Mark) < Double.valueOf(((BookInfo)arg1).Mark) ) {
				bookinfo = (BookInfo)arg0;
				arg0 = arg1;
				arg1 = bookinfo;
			}
			
			return 0;
		}
	}
}
