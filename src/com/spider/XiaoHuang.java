package com.spider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 爬虫下载图片工具类
 * 
 * @author Gym version 0.1
 * 
 * 
 */
public class XiaoHuang {

	// 已经存在的文件个数
	static int falseCount = 0;

	// 下载成功的文件个数
	static int successCount = 0;

	// 下载图片的文件名
	static String fileName = "";

	// 获取img标签正则
	private static final String IMGURL_REG = "<img.*src=(.*?)[^>]*?>";
	// 获取src路径的正则
	private static final String IMGSRC_REG = "[a-zA-z]+://[^\\s]*";
	// 获取图片Url的正则
	private static final String ImageUrl_REX = "(https|http):.{0,80}.(jpg|jpeg|png)";
	// 获取豆瓣美女二级页面的正则
	private static final String Second_REG = "(https|http)?:.{0,80}/topic/[1-9]{7}";
	// 草榴二级页面的正则
	private static final String Caoliu_Second = "htm_data.{0,80}.html";
	// 包含协议http的图片匹配
	private static final String CaoliuImageUrl_REX = "(https|http):.{0,80}.(jpg|jpeg|gif)";

	private static final String TopicName = "(https|http)://";
	// 存放连接失败的imageUrl集合
	static List<String> errorList = new ArrayList<String>();
	static Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
	
	static Proxy proxy5 = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 1080));

	/**
	 * 点击这里开始任务 2018-11-27 23:46分
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		System.setProperty("proxyPort", "80");
        System.setProperty("proxyHost", "127.0.0.1");
		System.out.println("下载任务开始了~~~~~");

		// 任务开始时间
		Date d1 = new Date();
	
		String targUrl = "http://verall80.tumblr.com/api/read/json?start=0&num=200";
//		String savePath = "D://log/Tumblr/verall80/";
//		List<String> regexList = getRegexList(targUrl, CaoliuImageUrl_REX);
//		for (String string : regexList) {
//			System.out.println(string);
//		}
		
		String content = getContent(targUrl);
		System.out.println(content);
		Matcher matcher = Pattern.compile(CaoliuImageUrl_REX).matcher(content);
		System.out.println(matcher.group());
//		Matcher matcher = Pattern.compile("(https|http)?:.{0,80}r.(jpg|jpeg)").matcher("src=https:\\/\\/66.media.tumblr.com\\/b8d6cedd806a590463a3eba314b2a294\\/tumblr_ph9jnoBfbK1rkr588_540.jpg\\");
//		System.out.println(matcher.group());
		
		
		
		
		
		
		// 下载完成的时间
		Date d2 = new Date();
		// 计算程序的运行时间，并输出
		long milsecond = d2.getTime() - d1.getTime();
		System.out.println("恭喜你,下载完成");
		System.out.println("本次任务一共用时： " + milsecond + "毫秒");
		System.out.println("有" + successCount + "个文件下载成功!");
		System.out.println("有" + falseCount + "个文件已经存在!");
		System.out.println("一共有坏链接" + errorList.size() + "个~~");
		
		
	}
	
	private static String getTopicName(String targUrl) {
		Matcher matcher = Pattern.compile(TopicName).matcher(targUrl);
		return matcher.group();
	}

	public static void buyerShow() {
		String url = "https://h5.m.taobao.com/ocean/privatenode/shop.html?sellerId=673336836";
		List<String> urlList = getRegexList(url, ImageUrl_REX);
		for (String imageUrl : urlList) {
			System.out.println(imageUrl);
		}
		
	}
	
	public static void downTumblr() {
		String  url = "";
		String dir = "D://log/Tumblr/hisame/";
		for (int i = 1; i <= 1; i++) {
			url = "http://hisame.tumblr.com/page/" + i;
			List<String> regexList = getRegexList(url, ImageUrl_REX);
			for (String imageUrl : regexList) {
				downLoadByProxy(imageUrl, dir);
			}
		}
		
		
		
	}
	
	
	private static void downLoadByProxy(String imageurl, String savePath) {
		String fileName = imageurl.substring(imageurl.lastIndexOf("/") + 1, imageurl.length());

		File file = new File(savePath + fileName);
		File saveDir = new File(savePath);
		// 判断文件夹是否存在,如果存在不下载
		// if(!saveDir.exists()) {
		// 新建文件夹
		saveDir.mkdirs();
		// 调用下载方法的时候,先检验相同目录下有没有相同的文件,如果没有才开始连接并下载
		if (!file.exists()) {

			try {
				URL url = new URL(imageurl);
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
				// 设置超时间为3秒
				conn.setConnectTimeout(3 * 1000);
				// 防止屏蔽程序抓取而返回403错误
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				
				// 得到输入流
				InputStream inputStream = conn.getInputStream();
				// 获取自己数组
				byte[] getData = readInputStream(inputStream);

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(getData);
				if (fos != null) {
					fos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			successCount++;
			// System.out.println("图片地址为:" + imageurl);
			System.out.println("第" + successCount + "个图片下载成功");

		} else {
			// System.out.println(savePath + fileName + "已经存在!");
			falseCount++;
			System.out.println("第" + falseCount + "个文件已存在");
		}
	}

	public static void downJianDan() {
		String url ="";
		String dir = "D://log/jandan/";
		String realImageUrl = "";
		for (int i = 43; i > 0; i--) {
			url = "http://jandan.net/ooxx/page-" + i;
			List<String> imageUrlList = getRegexList(url, ImageUrl_REX);
			for (String imageUrl : imageUrlList) {
				realImageUrl = imageUrl.replace("mw600", "large");
				downLoad403(realImageUrl, dir);
			}
			
			
		}
	}

	/**
	 * 下载1024的方法
	 * 
	 *
	 */
	public static void download1024() {
		String url = "";
		// String dir = "D://log/meizitu/";
		String dir = "";

		// 循环多个页面
		for (int pager = 1; pager < 200; pager++) {
			url = "https://bb.e8v.club/thread0806.php?fid=16&search=&page=" + pager;

			// 获取页面href标签
			List<String> secondUrlList = getRegexList(url, Caoliu_Second);

			// 校验页面是否包含二级topic页面
			if (secondUrlList.size() != 0) {
				for (String string : secondUrlList) {
					dir = "D://log/meizitu/" + string.substring(string.lastIndexOf("/") + 1, string.length()) + "/";
					// System.out.println("正在下载" + dir + "目录下的图片");
					List<String> realImgUrlList = getRegexList("https://bb.e8v.club/" + string, CaoliuImageUrl_REX);
					if (realImgUrlList != null) {
						for (String imageUrl : realImgUrlList) {
							if (imageUrl.contains("/u/")) {
								// 获取包含large的imageUrl
								try {
									downLoad403(imageUrl, dir);
									Thread.sleep(100);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}

		}
	}

	/**
	 * 获得指定url页面中符合regrex正则的url集合
	 * 
	 * @param url
	 * @param regrex
	 * @return
	 */
	public static List<String> getRegexList(String url, String regrex) {
		String content = null;
		List<String> list = new ArrayList<String>();
		try {
			content = getContent(url);
//			 System.out.println("content" + content);
			if (content != null) {
				Matcher matcher = Pattern.compile(regrex).matcher(content);
				while (matcher.find()) {
					if (!list.contains(matcher.group())) {
						// 一个页面内会有多个重复的topic,去重复
						list.add(matcher.group());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * 获取指定网页内容(页面源码)
	 * 
	 * ###如果发现获取的页面源码乱码,请查看网页的编码格式,并更改此处的编码格式
	 * 
	 * @param url
	 * @return String格式的页面源码
	 */
	public static String getContent(String url) {
		// 网页的编码格式
		String charset = "utf-8";
		// 利用URL解析网址
		String jsonString = "";
		URL urlObj = null;
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("The url was malformed!");
		}
		// URL连接
		URLConnection urlCon = null;
		try {
			// 打开URL连接
			urlCon = urlObj.openConnection(proxy);
			urlCon.addRequestProperty("user-agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.89 Safari/537.36");
			
			// 将HTML内容解析成UTF-8格式
			Document doc = Jsoup.parse(urlCon.getInputStream(), charset, url);
			jsonString = doc.toString();
		} catch (IOException e) {
			System.out.println("There was an error connecting to the URL");
			errorList.add(url);
			return "";
		}

		return jsonString;
	}

	/**
	 * 下载豆瓣的方法
	 * 
	 */
	public static void downloadDbmeinv() {
		String url = "";
		// String dir = "D://log/meizitu/";
		String dir = "";
		// 循环多个cid
		for (int cid = 2; cid < 8; cid++) {
			// 设置变量控制页面循环
			// boolean flag = true;
			// 循环多个页面
			for (int pager = 1; pager < 30; pager++) {
				url = "https://www.dbmeinv.com/index.htm?cid=" + cid + "&pager_offset=" + pager;
				// System.out.println("这是第" + cid + "个类型中的第" + pager + "页");
				dir = "D://log/meizitu/" + cid + "/";
				// 获取页面href标签
				List<String> secondUrlList = getRegexList(url, Second_REG);
				// 校验页面是否包含二级topic页面
				if (secondUrlList.size() != 0) {
					for (String string : secondUrlList) {
						List<String> realImgUrlList = getRegexList(string, ImageUrl_REX);
						if (realImgUrlList != null) {
							for (String imageUrl : realImgUrlList) {
								// 获取包含large的imageUrl
								if (imageUrl.contains("large")) {
									downLoad403(imageUrl, dir);
								}
							}
						}
					}
				}
			}
		}

		/**
		 * 这是正确的下载妹子图的代码块
		 */

		/*
		 * List<String> imageUrlList =
		 * getRegexList("https://www.dbmeinv.com/topic/1828105", ImageUrl_REX); for
		 * (String imageUrl : imageUrlList) { System.out.println(imageUrl); if
		 * (imageUrl.contains("large")) { download(imageUrl, dir); } }
		 */
	}

	/**
	 * 防止被403屏蔽的下载方法
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoad403(String imageurl, String savePath){

		String fileName = imageurl.substring(imageurl.lastIndexOf("/") + 1, imageurl.length());

		File file = new File(savePath + fileName);
		File saveDir = new File(savePath);
		// 判断文件夹是否存在,如果存在不下载
		// if(!saveDir.exists()) {
		// 新建文件夹
		saveDir.mkdirs();
		// 调用下载方法的时候,先检验相同目录下有没有相同的文件,如果没有才开始连接并下载
		if (!file.exists()) {

			try {
				URL url = new URL(imageurl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				// 设置超时间为3秒
				conn.setConnectTimeout(3 * 1000);
				// 防止屏蔽程序抓取而返回403错误
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				
				// 得到输入流
				InputStream inputStream = conn.getInputStream();
				// 获取自己数组
				byte[] getData = readInputStream(inputStream);

				FileOutputStream fos = new FileOutputStream(file);
				fos.write(getData);
				if (fos != null) {
					fos.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			successCount++;
			// System.out.println("图片地址为:" + imageurl);
			System.out.println("第" + successCount + "个图片下载成功");

		} else {
			// System.out.println(savePath + fileName + "已经存在!");
			falseCount++;
			System.out.println("第" + falseCount + "个文件已存在");
		}
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
