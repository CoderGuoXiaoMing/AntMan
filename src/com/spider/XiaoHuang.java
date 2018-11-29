package com.spider;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
	private static final String ImageUrl_REX = "(https|http)?:.{0,80}.(jpg|jpeg|png)+";
	// 获取二级页面的正则
	private static final String Second_REG = "(https|http)?:.{0,80}/topic/[1-9]{7}";
	//存放连接失败的imageUrl集合
	static List<String> errorList = new ArrayList<String>();
	
	/**
	 * 点击这里开始任务 2018-11-27 23:46分
	 * 
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws InterruptedException, IOException {

		System.out.println("下载任务开始了~~~~~");
		
		// 任务开始时间
		Date d1 = new Date();
		
		String url = "";
//		String dir = "D://log/meizitu/";
		String dir = "";
		//循环多个cid
		for (int cid = 2; cid < 8; cid++) {
			//设置变量控制页面循环
//			boolean flag = true;
			//循环多个页面
			for (int pager = 1; pager < 30 ; pager++) {
				url = "https://www.dbmeinv.com/index.htm?cid=" + cid + "&pager_offset=" + pager;
//				System.out.println("这是第" + cid + "个类型中的第" + pager + "页");
				dir = "D://log/meizitu/" + cid + "/";
				// 获取页面href标签
				List<String> secondUrlList = getRegexList(url, Second_REG);
				// 校验页面是否包含二级topic页面
				if (secondUrlList.size() != 0) {
					for (String string : secondUrlList) {
						List<String> realImgUrlList = getRegexList(string, ImageUrl_REX);
						if (realImgUrlList!=null) {
							for (String imageUrl : realImgUrlList) {
								//获取包含large的imageUrl
								if (imageUrl.contains("large")) {
									download(imageUrl, dir);
								}
							}
						}
					}
					
				}else {
//					System.out.println("第"+cid +"个类中第"+pager+"个页面没有二级页面");
					//此时页面中没有topic的标签,改变flag,跳出循环
//					flag = false;
//					break;
				}
//				Thread.sleep(100);
			}
		}
		
		
		
		/**
		 * 这是正确的下载妹子图的代码块
		 */
		
/*		List<String> imageUrlList = getRegexList("https://www.dbmeinv.com/topic/1828105", ImageUrl_REX);
		for (String imageUrl : imageUrlList) {
			System.out.println(imageUrl);
			if (imageUrl.contains("large")) {
				download(imageUrl, dir);
			}
		}
*/
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

	/**
	 * 获得指定url页面中符合regrex正则的url集合
	 * @param url
	 * @param regrex
	 * @return
	 */
	public static List<String> getRegexList(String url, String regrex) {
		String content = null;
		List<String> list = new ArrayList<String>();
		try {
			content = getContent(url);
			if (content != null) {
				Matcher matcher = Pattern.compile(regrex).matcher(content);
				while (matcher.find()) {
					if (!list.contains(matcher.group())) {
						// 一个页面内会有多个重复的topic,去重复
						list.add(matcher.group());
					}
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * 开始下载
	 * 
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public static String begin(String url) throws InterruptedException, IOException {
		URL urlObj = null;
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("The url was malformed!");
		}
		// URL连接
		URLConnection urlCon = null;
		urlCon = urlObj.openConnection();
		Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);
		String jsonString = doc.toString();
		return jsonString;
	}

	/**
	 * 获取页面源码中的realUrl地址
	 * 
	 * @param html
	 * @return
	 */
	public static List<String> getImageUrl(String html) {
		Matcher matcher = Pattern.compile(IMGURL_REG).matcher(html);
		List<String> listimgurl = new ArrayList<String>();
		while (matcher.find()) {
			listimgurl.add(matcher.group());
		}
		return listimgurl;
	}

	/**
	 * 获取页面的标签列表
	 * 
	 * @param listimageurl
	 * @return
	 */
	public static List<String> getImageSrc(List<String> listimageurl) {
		List<String> listImageSrc = new ArrayList<String>();
		for (String image : listimageurl) {
			Matcher matcher = Pattern.compile(IMGSRC_REG).matcher(image);
			while (matcher.find()) {
				listImageSrc.add(matcher.group().substring(0, matcher.group().length() - 1));
			}
		}
		return listImageSrc;
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
			urlCon = urlObj.openConnection();
			// 将HTML内容解析成UTF-8格式
			Document doc = Jsoup.parse(urlCon.getInputStream(), charset, url);
			jsonString = doc.toString();
		} catch (IOException e) {
//			System.out.println("There was an error connecting to the URL");
			errorList.add(url);
			return "";
		}

		return jsonString;
	}

	// 将网页中的电影图片下载到本地
	public static void getPictures(String imageUrl, String dir) throws InterruptedException {
		// 利用URL解析网址
		URL urlObj = null;
		try {
			urlObj = new URL(imageUrl);

		} catch (MalformedURLException e) {
			System.out.println("The url was malformed!");
		}

		// URL连接
		URLConnection urlCon = null;
		try {
			// 打开URL连接
			urlCon = urlObj.openConnection();
			// 将HTML内容解析成UTF-8格式
			Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", imageUrl);
			String jsonString = doc.toString();

			// 获得src的list集合
			List<String> imageSrc = getImageUrl(jsonString);
			// 将src的list转换为url的list
			List<String> imageUrlList = getImageSrc(imageSrc);
			// 开始循环下载任务
			String realUrl = "";
			for (int i = 0; i < imageUrlList.size(); i++) {
				realUrl = imageUrlList.get(i);
				if (realUrl.contains("large")) {
					download(realUrl, dir);
				}
			}

		} catch (IOException e) {
			System.out.println("There was an error connecting to the URL");
		}

	}

	// download()函数利用图片的url将图片下载到本地
	public static void download(String iamgeUrl, String dir) {
		fileName = iamgeUrl.substring(iamgeUrl.lastIndexOf("/") + 1, iamgeUrl.length());
		try {

			/*
			 * httpurl: 图片的url dirfile: 图片的储存目录
			 */
			URL httpurl = new URL(iamgeUrl);
			File dirfile = new File(dir);

			// 如果图片储存的目录不存在，则新建该目录
			if (!dirfile.exists()) {
				dirfile.mkdirs();
			}
			// 利用FileUtils.copyURLToFile()实现图片下载
			File f = new File(dir + fileName);

			// 对现有的目录里文件进行校验,如果存在就不下载
			if (!f.exists()) {

				FileUtils.copyURLToFile(httpurl, f);
				successCount++;
				System.out.println("第" + successCount + "个图片下载成功");

			} else {

//				System.out.println(fileName + "  有重复文件存在  " + dir + "  目录下");
				falseCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 进入此页面中的二级页面,正则匹配该页面中的href标签,返回二级页面的list集合
	 * 
	 * @param url
	 */
	public static List<String> ToSecondLevel(String url) {
		// 获取页面源码
		getContent(url);
		// 使用正则匹配获取所有的href标签内容

		return null;

	}
	
	public static void getPictsFromTopic(String topicUrl,String errordir) {
		List<String> regexList = getRegexList(topicUrl, ImageUrl_REX);
		for (String imageUrl : regexList) {
			if (imageUrl.contains("large")) {
				download(imageUrl, errordir);
			}
		}
	}

}
