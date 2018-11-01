package pnunu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pnunu.config.FilePathConfig;
import pnunu.model.MapModel;
import pnunu.util.SqliteUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Service
public class MapService {

    @Autowired
    private SqliteUtil sqliteUtil;

    public byte[] map(MapModel mapModel) throws Exception {
        return sqliteUtil.download(mapModel);
    }

    public void insert(String type) throws Exception {
        //创建一个线程池
        //ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        ExecutorService executor = Executors.newFixedThreadPool(16);
//        Map<String, FutureTask<Integer>> tasks = new HashMap<>();

        String path = FilePathConfig.getMapRoot() + File.separator + type;
        File[] x = new File(path).listFiles();
        if (x != null && x.length > 0) {
            for (File fx : x) {
                File[] y = fx.listFiles();
                if (y != null && y.length > 0) {
                    for (File fy : y) {
                        File[] z = fy.listFiles();
                        List<MapModel> models = new ArrayList<>();
                        if (z != null && z.length > 0) {
                            for (File fz : z) {
                                System.out.println(fz.getAbsolutePath());
                                int sx = Integer.valueOf(fx.getName());
                                int sy = Integer.valueOf(fy.getName());
                                int sz = Integer.valueOf(fz.getName().substring(0, fz.getName().indexOf(".")));
                                String imgSuffix = fz.getName().substring(fz.getName().indexOf("."));
                                models.add(new MapModel(type, sx, sy, sz, imgSuffix));
                            }
                        }
                        sqliteUtil.insert(models);
//                        SqliteCallable sqliteCallable = new SqliteCallable(models);
//                        FutureTask<Integer> task = new FutureTask<>(sqliteCallable);
//                        tasks.put(fx.getName() + "-" + fy.getName(), task);
//                        if (!executor.isShutdown()) {
//                            executor.submit(task);
//                        }
                    }
                }
            }
        }
//        // 关闭线程池
//        executor.shutdown();
    }

    class SqliteCallable implements Callable<Integer> {

        private List<MapModel> models;

        public SqliteCallable(List<MapModel> models) {
            this.models = models;
        }

        public List<MapModel> getModels() {
            return models;
        }

        public void setModels(List<MapModel> models) {
            this.models = models;
        }

        @Override
        public Integer call() throws Exception {
            return sqliteUtil.insert(models);
        }
    }
}
