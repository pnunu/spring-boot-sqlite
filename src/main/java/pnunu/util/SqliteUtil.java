package pnunu.util;

import org.springframework.stereotype.Component;
import pnunu.config.FilePathConfig;
import pnunu.model.MapModel;

import java.io.*;
import java.sql.*;
import java.util.List;

/**
 * @Author: pnunu
 * @Date: 2018/11/1 9:19
 */
@Component
public class SqliteUtil {

    public int insert(List<MapModel> models) throws Exception {
        byte[] b = null;
        Connection conn = getConnection(models.get(0).getDbName());
        for (int i = 0; i < models.size(); i++) {
            MapModel mapModel = models.get(i);
            InputStream inputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                String filepath = getFilePath(mapModel);
                inputStream = new FileInputStream(new File(filepath));
                byteArrayOutputStream = new ByteArrayOutputStream();
                int ch;
                while ((ch = inputStream.read()) != -1) {
                    byteArrayOutputStream.write(ch);
                }
                b = byteArrayOutputStream.toByteArray();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            }
            ByteArrayInputStream c = null;
            try {
                c = new ByteArrayInputStream(b);
                PreparedStatement pstmt = null;
                String sql = "insert into map (x, y, z, img) values (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, mapModel.getX());
                pstmt.setInt(2, mapModel.getY());
                pstmt.setInt(3, mapModel.getZ());
                pstmt.setBinaryStream(4, c, c.available());
                pstmt.executeUpdate();
                if (i % 50 == 0)
                    conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        conn.commit();
        conn.close();
        return models.size();
    }


    public byte[] download(MapModel mapModel) throws Exception {
        String filepath = getFilePath(mapModel);
        if (new File(filepath).exists()) {
            return readFile(filepath);
        }
        Connection conn = getConnection(mapModel.getDbName());
        Statement sta = null;
        ResultSet rs = null;
        InputStream input = null;
        FileOutputStream out = null;
        try {
            sta = conn.createStatement();
            // image字段为BLOG字段
            String sql = "Select x,y,z,img from map where x = " + mapModel.getX() + " and y = " + mapModel.getY() + " and z = " + mapModel.getZ();
            rs = sta.executeQuery(sql);
            while (rs.next()) {
                // 输出文件名
                byte[] blob = rs.getBytes("img");
                makdirs(filepath);
                System.out.println("输出文件路径为:" + filepath);
                input = new ByteArrayInputStream(blob);
                out = new FileOutputStream(filepath);
                int len = blob.length;
                byte[] buffer = new byte[len];
                while ((len = input.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                return buffer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (input != null) {
                    input.close();
                }
                if (rs != null)
                    rs.close();
                if (sta != null)
                    sta.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private byte[] readFile(String filepath) throws Exception {
        File file = new File(filepath);
        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);
        if (input != null)
            input.close();
        return byt;
    }

    private void makdirs(String filepath) {
        File f = new File(filepath);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
    }

    public static String getFilePath(MapModel mapModel) {
        String filepath = FilePathConfig.getMapRoot() + File.separator + mapModel.getType() + File.separator + mapModel.getX()
                + File.separator + mapModel.getY() + File.separator + mapModel.getZ() + mapModel.getImgSuffix();
        return filepath;
    }

    /**
     * 获得Connection
     */
    public static Connection getConnection(String type) {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db/" + type + ".db");
            conn.setAutoCommit(false);//关闭自动提交，可以实现多次添加数据，一次提交，提高效率（对于插入一条数据可以不关上此功能）
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


}
