package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtilss {

	public static String readTxt(String filePath) {
		StringBuilder sb = new StringBuilder();
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				BufferedReader br = new BufferedReader(isr);
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					sb.append(lineTxt);
				}
				br.close();
			} else {
				System.out.println("文件不存在!");
			}
		} catch (Exception e) {
			System.out.println("文件读取错误!");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String filePath = "D:\\log\\Tumblr\\leg.txt";
		String readTxt = readTxt(filePath);
		System.out.println(readTxt);
		System.out.println(readTxt.length());
		
	}

}
