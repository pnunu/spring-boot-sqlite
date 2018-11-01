package pnunu.pool;

import org.springframework.context.annotation.Lazy;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: pnunu
 * @Date: 2018/11/1 16:05
 * @Description: 连接池 工具类
 */
@Lazy
public class PoolUtil implements Pool {
    /**
     * 此处可以设置成 读取 配置文件的方式
     */
    private static String jdbcDriver = "org.sqlite.JDBC"; // 数据库驱动
    private static String dbUrl = "jdbc:sqlite:db/map01.db"; // 数据 URL
    private static String dbUsername = ""; // 数据库用户名
    private static String dbPassword = ""; // 数据库用户密码

    private static ConnectionPool connPool = null;

    // 设置成单例模式 防止 多次实例化连接池
    private static PoolUtil instance = new PoolUtil();

    private PoolUtil() {
    }

    public static PoolUtil getInstance() {
        return instance;
    }

    static {
        connPool = new ConnectionPool(jdbcDriver, dbUrl, dbUsername, dbPassword);
    }

    @Override
    public int getInitialConnections() {

        return connPool.getInitialConnections();
    }

    @Override
    public void setInitialConnections(int initialConnections) {

        connPool.setInitialConnections(initialConnections);
    }

    @Override
    public int getIncrementalConnections() {

        return connPool.getIncrementalConnections();
    }

    @Override
    public void setIncrementalConnections(int incrementalConnections) {

        connPool.setIncrementalConnections(incrementalConnections);
    }

    @Override
    public int getMaxConnections() {

        return connPool.getMaxConnections();
    }

    @Override
    public void setMaxConnections(int maxConnections) {

        connPool.setMaxConnections(maxConnections);
    }

    @Override
    public void initPool() {
        try {
            connPool.createPool();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() {

        Connection conn = null;
        try {
            conn = connPool.getConnection();
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void returnConnection(Connection conn) {

        connPool.returnConnection(conn);
    }

    @Override
    public void refreshConnections() {

        try {
            connPool.refreshConnections();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void closeConnectionPool() {

        try {
            connPool.closeConnectionPool();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


}
