package com.heye.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UrlQueue {
	
	private static Set<String>   GradedUrl = new HashSet<String>();			// 未解析的URL队列
	private static Queue<String> UnGradUrl = new PriorityQueue<String>();	// 解析过的URL队列
	
	public static List<String> BookUrl = new ArrayList<String>();
	
	public static Lock Mutex = new ReentrantLock();
	
	// 获得未解析URL队列
	public static Queue<String> GetUnVisitedUrl() {
		return UnGradUrl;
	}
	
	// 添加到解析过的URL队列中
	public static void AddVisitedUrl( String url ) {
		GradedUrl.add( url );
		return;
	}
	
	// 移除解析过的URL
	public static void RemoveVisitedUrl( String url ) {
		GradedUrl.remove( url );
		return;
	}
	
	// 获得一个未解析URL
	public static Object UnVisitedUrlDeQueue() {
		return UnGradUrl.poll();
	}
	
	// 添加到未解析URL队列中
	public static void AddUnvisitedUrl( String url ) {
		if ( url != null && !url.trim().equals("") && !GradedUrl.contains(url) && !UnGradUrl.contains(url) ) {
			UnGradUrl.add( url );
		}
		
		return;
	}
	
	// 获得已经解析的URL数目
	public static int GetGradedUrlNum() {
		return GradedUrl.size();
	}
	
	// 判断未解析的URL队列中是否为空
	public static boolean UnGradUrlsEmpty() {
		return UnGradUrl.isEmpty();
	}
	
}
