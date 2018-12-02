package com.spider;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		File dir = new File("D://log/meizitu");
		// System.out.println(dir.length());
		File[] listFiles = dir.listFiles();
		for (File file : listFiles) {
			if(file!= null) {
				if(file.listFiles()!= null && file.listFiles().length == 0 
						) {
					file.delete();
					System.out.println("删除了" + file.getName());
				}
			}
		}
	}
}
