package com.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		String CaoliuImageUrl_REX = "https\\s?(jpg|jpeg|gif)";
		String aString = "(https|http)?:.{0,80}.(jpg|jpeg)";
		String content = 
				"https:\\/\\/66.media.tumblr.com\\/b8d6cedd806a590463a3eba314b2a294\\/tumblr_ph9jnoBfbK1rkr588_540.jpg";
		Matcher matcher = Pattern.compile(aString).matcher(content);
		System.out.println(matcher.group());
	}
}
