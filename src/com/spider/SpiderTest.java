package com.spider;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.utils.Utils;

public class SpiderTest {

	
	public static void main(String[] args) throws AWTException, IOException {
		
		String path = "D:\\url.txt";
		String urls = readAndOut(path);
		System.out.println("urls:      " + urls);
		/*
		 * 正则匹配链接
		 */
		String pa1 = "(https|http|ftp).{0,80}.(jpg|jpeg|png|gif)";
		Pattern r1 = Pattern.compile(pa1);
		Matcher m1 = r1.matcher(urls);
		
		/*
		 * 下载路径
		 */
		String dir = "D://log/reddit/";
		System.out.println("任务要开始了");
		int count = 0;
		
		/*
		 * 循环下载数据
		 */
		Date d1 = new Date();
//		String[] list = urls.split("http");
//		for (int i = 1; i < list.length; i++) {
//			System.out.println("http" + list[i]);
//			download("http" + list[i], dir, i + "");
//		}
		while(m1.find()) {
			System.out.println("进来了~~~~~~");
//			System.out.println(m1.group() + "   ====old");
//			if(m1.group().indexOf("thumb180") > -1) {
				//替换链接中的赋好
//				String s = "\\";
//				String replaceAll = m1.group().replaceAll("\\\\", "");
//				System.out.println(replaceAll);
//				String replaceAll = m1.group().replaceAll("thumb180", "bmiddle");
				
				String[] split = m1.group().split("/");
////				replaceAll = replaceAll.split("<img src=\"//")[1];
				download(m1.group(), dir, split[split.length - 1]);
				count++;
				System.out.println("第"+count+"张图片下载成功~~~~");
////			}
		}
		Date d2 = new Date();
		int seconds = (int) ((d2.getTime()-d1.getTime())/1000);
		System.out.println("一共下载了" + seconds + "秒");
		System.out.println("恭喜你,下载完成了");
		
		
		
	}
	
	/*
	 * 从txt文档中读取url
	 */
	public static String readAndOut(String path) throws IOException {
//		FileReader fr=new FileReader("D:\\workspace\\MyLearn\\count.txt");
		FileReader fr=new FileReader(path);
        //可以换成工程目录下的其他文本文件
        BufferedReader br=new BufferedReader(fr);
        String s = "";
        while(br.readLine()!=null){
            s += br.readLine();
        }
        br.close();
        return s;
		
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
				}
				// 利用FileUtils.copyURLToFile()实现图片下载
				File f = new File(dir + filename);

				// 对现有的目录里文件进行校验,如果存在就不下载
				if (!f.exists()) {

					FileUtils.copyURLToFile(httpurl, f);
					Utils.recordDownloadUrl(url);
//					System.out.println("像一棵海草,浪花里舞蹈~~~~~");
				} else {

					System.out.println(filename + "  有重复文件存在  " + dir + "  目录下");

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
}
