package com.spider;

public class Test {

	public static void main(String[] args) {
//		String CaoliuImageUrl_REX = "https\\s?(jpg|jpeg|gif)";
//		String aString = "(https|http)?:.{0,80}.(jpg|jpeg)";
//		String content = 
//				"https:\\/\\/66.media.tumblr.com\\/b8d6cedd806a590463a3eba314b2a294\\/tumblr_ph9jnoBfbK1rkr588_540.jpg";
//		Matcher matcher = Pattern.compile(aString).matcher(content);
//		System.out.println(matcher.group());
		
		String content = "start\":1000,\"posts-total\":100721,\"posts-type\":false,";
		String[] split = content.split(",\"posts-type");
		String[] split2 = split[0].split("posts-total\":");
		int count = Integer.parseInt(split2[1]);
		System.out.println(count);
	}
}
