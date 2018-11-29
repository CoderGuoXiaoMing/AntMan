package com.utils;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Utils {
	
	// 驱动类类名
	private static final String DBDRIVER = "com.mysql.jdbc.Driver";
	// 数据库名
	private static final String DBNAME = "test";
	// 连接URL
	private static final String DBURL = "jdbc:mysql://localhost:3306/" + DBNAME;
	// 数据库用户名
	private static final String DBUSER = "root";
	// 数据库密码
	private static final String DBPASSWORD = "chenyanan";

	private static Connection conn = null;

	private static PreparedStatement ps = null;

	private static ResultSet rs = null;

	/*
	 * 获取数据库连接
	 */
	public static Connection getConnection() {
		try {
			Class.forName(DBDRIVER);
		} catch (ClassNotFoundException e) {
			// 捕获驱动类无法找到异常
			System.out.println("找不到MYSQL驱动程序");
			System.out.println(e.toString());
			e.printStackTrace();
		}

		// 注册驱动
		try {

			// 获得连接对象
			conn = (Connection) DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		} catch (SQLException e) {
			// 捕获SQL异常
			System.out.println("加载MYSQL驱动程序失败~~~");
			e.printStackTrace();
		}
		return conn;
	}

	/*
	 * 查询数据
	 */
	public static int select(String sql) throws Exception {
		try {
			conn = getConnection();
			ps = (PreparedStatement) conn.prepareStatement(sql);
			rs = ps.executeQuery(sql);
			rs.last();
			return rs.getRow(); // 得到当前行号，即结果集记录数
		} catch (SQLException sqle) {
			throw new SQLException("select data Exception: " + sqle.getMessage());
		}
		finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new Exception("conn close exception: " + e.getMessage());
			}
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				throw new Exception("rs close exception: " + e.getMessage());
			}
		}
	}
	
	 public static String dateToStamp() throws ParseException{
		 	SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Long time=new Long(new Date().getTime());  
		    String d = format.format(time);
	        return d;
	    }
	
	/*
	 * 增删改均调用这个方法
	 */

	public static void updata(String sql) throws Exception {
		try {
			conn = getConnection();
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			throw new SQLException("insert data Exception: " + sqle.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e) {
				throw new Exception("ps close exception: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				throw new Exception("conn close exception: " + e.getMessage());
			}
		}
	}
	
	
	public static void recordDownloadUrl(String url) throws Exception {
		//因为在表中对数据的重复性已经做了校验,所以如果查询的时候要加上limit 1 ,可以保证在数据库查到数据的时候就停止了
		String query_sql = "select * from image where url = '" + url + "' LIMIT 1";
		String createDate = dateToStamp();
		String update_sql = "insert into image values ('" + url + "','" + createDate + "')";
		int flag = select(query_sql);
		if(flag == 0 ) {
			try {
				Utils.updata(update_sql);
			} catch (Exception e) {
				System.out.println("增删改失败~~~~");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
