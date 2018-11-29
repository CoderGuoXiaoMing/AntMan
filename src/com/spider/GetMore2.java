package com.spider;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class GetMore2 {
	/*
	 * 知乎福利download
	 */

	/*
	 * *************是否开启下载*************** 
	 * 0 代表不下载 
	 * 1 代表下载
	 */
	static int flag = 1;
	
	// 设置的连接超时时间(单位为秒)
	static int timeLimit = 10;

	// 每次下载的回答个数
	static int limit = 2;

	// 问题的ID
	static int questId = 25550911;

	// 已经存在的文件个数
	static int falseCount = 0;

	// 下载成功的文件个数
	static int successCount = 0;

	static String mainUrl = "https://www.zhihu.com/api/v4/questions/" + questId
			+ "/answers?include=data[*].is_normal,admin_closed_comment,reward_info,is_collapsed,annotation_action,"
			+ "annotation_detail,collapse_reason,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,"
			+ "editable_content,voteup_count,reshipment_settings,comment_permission,created_time,updated_time,review_info,"
			+ "relevant_info,question,excerpt,relationship.is_authorized,is_author,voting,is_thanked,is_nothelp;data[*].mark_infos[*].url;"
			+ "data[*].author.follower_count,badge[*].topics&limit=" + limit + "&offset=";

	
	/**
	 * 点击这里开始任务
	 * 2018-11-27 23:46分
	 */
	public static void main(String[] args) {
			begin(999999999);
	}
	
	/**
	 * 开始下载
	 */
	public  static long begin(int l) {
		System.out.println("下载任务开始了~~~~~");
		//获取回答的个数
		int answerCount = getAnswerCount();
		System.out.println("获取回答个数成功-------------   " + answerCount + "   个");
		//任务开始时间
		Date d1 = new Date();
		//任务下载开关
		if (flag == 1) {
			/*
			 * **********************************如果没有开始下载就检查这里的flag-----------------
			 */
			System.out.println("开始下载图片了~~~~");
			// 根据回答的个数,对每个回答进行下载
			for (int i = 0; i < answerCount; i++) {
				// 把回答数拼接到url中
				String mainUrl1 = mainUrl + i + "&sort_by=default";
				// 图片储存目录
				String dir = "D://log/zhihu/" + questId + "/" + l + "/"; 
				getPictures(mainUrl1, dir);
				i = i + limit - 1;
			}
		}
		System.out.println("图片下载完毕！");
		//下载完毕的时间
		Date d2 = new Date();
		// 计算程序的运行时间，并输出
//		long seconds = (d2.getTime() - d1.getTime()) / 1000;
		long milsecond = d2.getTime() - d1.getTime();
		System.out.println("恭喜你,下载完成");
		System.out.println("本次任务一共用时： " + milsecond + "秒.");
		System.out.println("有" + successCount + "个文件下载成功!");
		System.out.println("有" + falseCount + "个文件已经存在!");
		return milsecond;
	}

	// getContent()函数: 将网页中的电影图片下载到本地
	public static void getPictures(String url, String dir) {
		// 利用URL解析网址
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
			// 设置超时时间
//			urlCon.setConnectTimeout(timeLimit * 1000);
//			urlCon.setReadTimeout(timeLimit * 1000);

			// 将HTML内容解析成UTF-8格式
			Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);
			// System.out.println(doc.toString());
			String jsonString = doc.toString();

			String pattern = "(https|http)?:.{0,80}.(jpg|jpeg|png)";

			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(jsonString);
			ArrayList<String> list = new ArrayList<String>();

			// 开始循环下载
			while (m.find()) {
				if (!list.contains(m.group())) {
					System.out.println("我匹配到了");
					System.out.println(m.group());
					list.add(m.group());
					String[] split = m.group().split("/");
//					download(m.group(), dir, split[split.length - 1]);
				}
			}
		} catch (IOException e) {
			System.out.println("There was an error connecting to the URL");
		}

	}

	// download()函数利用图片的url将图片下载到本地
	public static void download(String url, String dir, String filename) {
		try {

			/*
			 * httpurl: 图片的url dirfile: 图片的储存目录
			 */
			URL httpurl = new URL(url);
			File dirfile = new File(dir);

			// 如果图片储存的目录不存在，则新建该目录
			if (!dirfile.exists()) {
				dirfile.mkdirs();
			}else {
//				System.out.println("该问题已经下载过了");
			}
			// 利用FileUtils.copyURLToFile()实现图片下载
			File f = new File(dir + filename);

			// 对现有的目录里文件进行校验,如果存在就不下载
			if (!f.exists()) {

				FileUtils.copyURLToFile(httpurl, f);
				successCount++;
//				System.out.println("第" + successCount + "个图片下载成功");

			} else {

				System.out.println(filename + "  有重复文件存在  " + dir + "  目录下");
				falseCount++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * 检测该问题下的回答数量
	 */
	public static int getAnswerCount() {

		/*
		 * 统计出来的回答个数
		 */
		int count = 0;

		// 下载的连接
		String url = "https://www.zhihu.com/api/v4/questions/" + questId
				+ "/answers?include=data[*].is_normal,admin_closed_comment,reward_info,is_collapsed,annotation_action,"
				+ "annotation_detail,collapse_reason,is_sticky,collapsed_by,suggest_edit,comment_count,can_comment,content,"
				+ "editable_content,voteup_count,reshipment_settings,comment_permission,created_time,updated_time,review_info,"
				+ "relevant_info,question,excerpt,relationship.is_authorized,is_author,voting,is_thanked,is_nothelp;data[*].mark_infos[*].url;"
				+ "data[*].author.follower_count,badge[*].topics&limit=" + 1 + "&offset=" + 0 + "&sort_by=default";

		// 循环测试回答的数量

		// 利用URL解析网址
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

			// 设置超时时间
//			urlCon.setConnectTimeout(timeLimit * 1000);
//			urlCon.setReadTimeout(timeLimit * 1000);

			// 将HTML内容解析成UTF-8格式
			Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);

			// System.out.println(doc.toString());
			String jsonString = doc.toString();
			String[] split = jsonString.split(",\"totals\":");
			String[] split2 = split[1].split("}}");
			count = Integer.parseInt(split2[0]);

		} catch (IOException e) {
			System.out.println("There was an error connecting to the URL");
		}

		return count;
	}
	
}
