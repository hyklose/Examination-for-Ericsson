package com.heye.modules;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.heye.common.LinkFilter;

public class UrlSpider {
	
	// 获取�?个网站上的链�?,filter用来过滤链接
	public static Set<String> ExtracLinks( String url, LinkFilter filter ) {
		
		Set<String> links = new HashSet<String>();
		
		try {
			Parser parser = new Parser( url );
			parser.setEncoding("utf-8");
			
			// 过滤 <frame >标签�? filter，用来提�? frame 标签里的 src 属�?�所表示的链�?
			NodeFilter frameFilter = new NodeFilter() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public boolean accept( Node node ) {
					if ( node.getText().startsWith("frame src=") ) {
						return true;
					}
					else {
						return false;
					}
				}
			};
			
			// OrFilter 来设置过�? <a> 标签，和 <frame> 标签
			OrFilter linkFilter = new OrFilter(new NodeClassFilter( LinkTag.class), frameFilter );
			
			// 得到�?有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch( linkFilter );
			// 提取标签
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				// <a> 标签
				if (tag instanceof LinkTag) {
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();
					if ( filter.accept(linkUrl) ) {
						links.add( linkUrl );
					}
				}
				// <frame> 标签
				else {
					// 提取 frame �? src 属�?�的链接�? <frame src="test.html"/>
					String frame = tag.getText();
					int start = frame.indexOf( "src=" );
					frame = frame.substring( start );
					int end = frame.indexOf( " " );
					if ( end == -1 ) {
						end = frame.indexOf( ">" );
					}
					String frameUrl = frame.substring( 5, end - 1 );
					if ( filter.accept(frameUrl) ) {
						links.add( frameUrl );
					}
				}
			}
		}
		catch (ParserException e) {
			e.printStackTrace();
		}
		
		return links;
	}
	
	public static Set<String> ExtracLinks2( String url, LinkFilter filter ) {
		Set<String> links = new HashSet<String>();
		
		try {
			//Document document = Jsoup.connect(url).header("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17").get();
			Document document = Jsoup.connect(url).data("query", "Java")
					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.56 Safari/537.17")
					.cookie("auth", "token")
					.timeout(3000)
					.post();
			Elements elements = document.select( "a[href]");
			
			for( Element element : elements ) {
				links.add( element.attr( "abs:href" ) );
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return links;
	}
}
