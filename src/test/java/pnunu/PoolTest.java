package pnunu;

import pnunu.pool.DynamicPoolUtil;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author: pnunu
 * @Date: 2018/11/1 16:06
 * @Description: 测试连接池
 */
public class PoolTest {
    public static void main(String[] args) throws Exception {
//        PoolUtil pool = PoolUtil.getInstance();
//        pool.initPool();
//        Connection connection = pool.getConnection();
//        System.out.println("connection:" + connection);
        //pool.returnConnection(connection);
        Connection conn = DynamicPoolUtil.getConnection("roadmap", "jdbc:sqlite:db/roadmap.db");
        Statement sta = null;
        ResultSet rs = null;
        InputStream input = null;
        FileOutputStream out = null;
        try {
            sta = conn.createStatement();
            String sql = "select count(1) as count from map";
            rs = sta.executeQuery(sql);
            while (rs.next()) {
                // 输出文件名
                System.out.println(rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("");
    }


}
