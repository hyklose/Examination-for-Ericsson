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
	
	private static Set<String>   GradedUrl = new HashSet<String>();			// δ������URL����
	private static Queue<String> UnGradUrl = new PriorityQueue<String>();	// ��������URL����
	
	public static List<String> BookUrl = new ArrayList<String>();
	
	public static Lock Mutex = new ReentrantLock();
	
	// ���δ����URL����
	public static Queue<String> GetUnVisitedUrl() {
		return UnGradUrl;
	}
	
	// ��ӵ���������URL������
	public static void AddVisitedUrl( String url ) {
		GradedUrl.add( url );
		return;
	}
	
	// �Ƴ���������URL
	public static void RemoveVisitedUrl( String url ) {
		GradedUrl.remove( url );
		return;
	}
	
	// ���һ��δ����URL
	public static Object UnVisitedUrlDeQueue() {
		return UnGradUrl.poll();
	}
	
	// ��ӵ�δ����URL������
	public static void AddUnvisitedUrl( String url ) {
		if ( url != null && !url.trim().equals("") && !GradedUrl.contains(url) && !UnGradUrl.contains(url) ) {
			UnGradUrl.add( url );
		}
		
		return;
	}
	
	// ����Ѿ�������URL��Ŀ
	public static int GetGradedUrlNum() {
		return GradedUrl.size();
	}
	
	// �ж�δ������URL�������Ƿ�Ϊ��
	public static boolean UnGradUrlsEmpty() {
		return UnGradUrl.isEmpty();
	}
	
}
