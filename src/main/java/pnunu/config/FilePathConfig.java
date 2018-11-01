package pnunu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: pnunu
 * @Date: 2018/10/24 16:27
 * @Description: 文件路径配置
 */
@Component
public class FilePathConfig {

    private static String mapRoot;

    public static String getMapRoot() {
        return mapRoot;
    }

    @Value("${map.mapRoot}")
    public void setMapRoot(String mapRoot) {
        FilePathConfig.mapRoot = mapRoot;
    }

}
