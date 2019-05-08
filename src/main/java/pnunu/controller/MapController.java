package pnunu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pnunu.model.MapModel;
import pnunu.service.MapService;
import pnunu.util.SqliteUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequestMapping
public class MapController {

    @Autowired
    private MapService mapService;

    @RequestMapping(value = "/{type}/{x}/{y}/{z}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public Object map(@PathVariable("type") String type, @PathVariable("x") int x,
                      @PathVariable("y") int y, @PathVariable("z") String z) throws Exception {
        byte[] bytes = mapService.map(new MapModel(type, x, y, Integer.valueOf(z.substring(0, z.indexOf(".")))));
        return bytes;
    }

    @RequestMapping(value = "/maps/{type}/{x}/{y}/{z}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public BufferedImage getImage(@PathVariable("type") String type, @PathVariable("x") int x,
                                  @PathVariable("y") int y, @PathVariable("z") String z) throws Exception {
        MapModel model = new MapModel(type, x, y, Integer.valueOf(z.substring(0, z.indexOf("."))));
        BufferedImage image = null;
        try {
            image = ImageIO.read(new FileInputStream(new File(SqliteUtil.getFilePath(model))));
        } catch (Exception e) {
        }
        if (image == null) {
            mapService.map(model);
        }
        try {
            image = ImageIO.read(new FileInputStream(new File(SqliteUtil.getFilePath(model))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    @ResponseBody
    @RequestMapping(value = "/initMap")
    public String init(String type) throws Exception {
        mapService.insert(type);
        return "";
    }
}
