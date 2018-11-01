package pnunu.pool;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: pnunu
 * @Date: 2018/11/1 16:49
 * @Description: 根据不同的类型获取connection
 */
public class DynamicPoolUtil {
    private static Map<String, ConnectionPool> map = new ConcurrentHashMap<>();

    private static ConnectionPool createConnectionPool(String jbcDriver, String dbUrl) throws Exception {
        ConnectionPool connectionPool = new ConnectionPool(jbcDriver, dbUrl, "", "");
        connectionPool.createPool();
        return connectionPool;
    }

    private static String JDBC_DRIVER = "org.sqlite.JDBC";

    public static Connection getConnection(String type, String url) throws Exception {
        if (map.get("type") == null) {
            map.put(type, createConnectionPool(JDBC_DRIVER, url));
        }
        Connection conn = map.get(type).getConnection();
        return conn;
    }
}
