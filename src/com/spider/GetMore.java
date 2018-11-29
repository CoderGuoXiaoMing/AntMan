package com.spider;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.utils.Utils;

/*
 * 福利吧download
 */
public class GetMore {
	
	/*
	 * 下载的参数
	 */
	static int begin =   2018100  ;
	static int lastest =   2018140  ;
	static String kindOfImage = "jpg";  // all 下载所有
/*   			           ^
*			              ^^^
*			             ^^^^^
*			            ^^^^^^^
*			           ^^^^^^^^^
*			              ^^^
*			              ^^^
*			              ^^^
*			              ^^^
*						  ^^^
*			                               修改这里的参数
*/	
	
	static int downloadNum = 0;
	
	public static void main(String[] args) {
        Date d1 = new Date();
        
        System.out.println("屯粮食开始了......");
       
        for (int i = begin ; i < lastest + 1 ; i++) {
			
        	String mainUrl= "http://f.uliba.net/"+ i +".html/";
//        	String mainUrl= "http://f.uliba.net/%e7%be%8e%e8%85%bf%e4%b8%9d%e8%a2%9c.html";
        	System.out.println("mainUrl       " + mainUrl);
        	ArrayList<String> urls = new ArrayList<String>();
        	urls.add(mainUrl);
        	
        	for(int j=2; j<=4; j++) {
        		
        		urls.add(mainUrl + Integer.toString(j));
        		
        	}
        	// 图片储存目录
        	String dir = "D:\\log\\fuliba\\" + i + "\\"; 
        	System.out.println("dir:       " + dir);
        	
        	// 利用循环下载每个页面中的图片
        	for(String url: urls) {
        		
        		getPictures(url, dir);
        		
        	}
		}
        
        System.out.println("程序运行完毕！");
        Date d2 = new Date();
        
        // 计算程序的运行时间，并输出
        long seconds = (d2.getTime()-d1.getTime())/1000;
        System.out.println("一共用时： "+seconds+"秒.");
        
    }
    
    // getContent()函数: 将网页中的电影图片下载到本地
    public static void getPictures(String url, String dir){
        
        // 利用URL解析网址
        URL urlObj = null;
        try{
        	
            urlObj = new URL(url);

        }
        catch(MalformedURLException e){
            System.out.println("The url is wrong!");
        }

        // URL连接
        URLConnection urlCon = null;
        try{
            // 打开URL连接
            urlCon = urlObj.openConnection(); 
            //设置超时时间
            urlCon.setConnectTimeout(30*1000);
            urlCon.setReadTimeout(30*1000); 
            // 将HTML内容解析成UTF-8格式
            Document doc = Jsoup.parse(urlCon.getInputStream(), "utf-8", url);
            //获取所有img标签的内容
            Elements elems = doc.getElementsByTag("img");
           // Elements pic_block = elems.first().getElementsByTag("a");
            System.out.println("img的标签个数:   "+ elems.size());
            for(int i=0; i<elems.size(); i++) {
                // 提取电影图片的url, name
                String picture_url = elems.get(i).getElementsByTag("img").attr("src");
            
                String[] split = picture_url.split("/");
                int length = split.length;
                String picture_name = split[length-1];
                //选择,只下载jpg图片
                if("all".equals(kindOfImage)) {
                	
                downloadAll(picture_url, dir, picture_name);
                
                }else {
                	
                downloadImage(picture_url, dir, picture_name);
                	
                }
                downloadNum++;	
                System.out.println("第"+(downloadNum+1)+"张图片下载完毕！");
            }
            
        }
        catch(IOException e){
            System.out.println("There was an error connecting to the URL");
        }

    }
    
    // download()函数利用图片的url将图片下载到本地
    public static void downloadAll(String url, String dir, String filename) {  
        try { 
            
            /* httpurl: 图片的url
             * dirfile: 图片的储存目录
             */
            URL httpurl = new URL(url);  
            File dirfile = new File(dir); 
            
            // 如果图片储存的目录不存在，则新建该目录
            if (!dirfile.exists()) {    
            	System.out.println("文件夹不存在");
                dirfile.mkdirs();  
            }  
            
            // 利用FileUtils.copyURLToFile()实现图片下载
            File f = new File(dir+filename);
            
            //对现有的目录里文件进行校验,如果存在就不下载
            if(!f.exists()) {
            	
            	FileUtils.copyURLToFile(httpurl, f);
            	//将成功下载的url插入数据库
            	
            }else {
            	
            	System.out.println( filename + "  有重复文件存在  " + dir + "  目录下");
            }
            //向数据库中插入数据
            Utils.recordDownloadUrl(url);
        } 
        catch(Exception e) {  
            e.printStackTrace();  
        }  
    }
    
    /*
     * 只下载图片,不下载动态图片
     */
    public static void downloadImage(String url, String dir, String filename) {
    	if(url.endsWith("jpg")) {
        	
          downloadAll(url, dir, filename);
          	
    	}
    }
}
