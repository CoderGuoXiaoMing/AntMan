package com.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;

public class C3p0Utils {

    // 获得c3p0连接池对象
    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    /**
     * 获得数据库连接对象
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 获得c3p0连接池对象
     * @return
     */
    public static DataSource getDataSource() {
        return ds;
    }
    
  //释放连接回连接池
    public static void closeAll(Connection conn,PreparedStatement pst,ResultSet rs){  
           if(rs!=null){  
               try {  
                   rs.close();  
               } catch (SQLException e) {  
            	   System.out.println("rs连接关闭异常");
               }  
           }  
           if(pst!=null){  
               try {  
                   pst.close();  
               } catch (SQLException e) {  
            	   System.out.println("pst连接关闭异常");
               }  
           }  
     
           if(conn!=null){  
               try {  
                   conn.close();  
               } catch (SQLException e) {  
            	   System.out.println("conn连接关闭异常");
               }  
           }  
       }  
}