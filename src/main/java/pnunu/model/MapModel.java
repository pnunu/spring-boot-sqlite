package pnunu.model;

public class MapModel {
    private String type = "roadmap";
    private String dbName;
    private int x;
    private int y;
    private int z;
    private byte[] img;
    private String imgSuffix = ".png";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getImgSuffix() {
        return imgSuffix;
    }

    public void setImgSuffix(String imgSuffix) {
        this.imgSuffix = imgSuffix;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public MapModel() {
    }

    public MapModel(String type, int x, int y, int z, String imgSuffix) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.imgSuffix = imgSuffix;
        toDbName();
    }

    public MapModel(String type, int x, int y, int z) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        toDbName();
    }



    private final static String ROAD_MAP = "roadmap";
    private final static String TERRAIN = "terrain";
    private final static String SATELLITE = "satellite";

    private void toDbName() {

        if (ROAD_MAP.equals(type)) {
            setDbName(this.type);
        } else if (TERRAIN.equals(type)) {
            if (x <= 16) {
                setDbName(type + "-1");
            } else if (x == 17) {
                setDbName(type + "-2");
            } else if (x == 18) {
                if (y <= 215883) {
                    setDbName(type + "-3");
                } else {
                    setDbName(type + "-4");
                }
            }
        } else if (SATELLITE.equals(type)) {
            if (x <= 15) {
                setDbName(type + "-1");
            } else if (x == 16) {
                if (y <= 53970) {
                    setDbName(type + "-3");
                } else {
                    setDbName(type + "-4");
                }
            } else if (x == 17) {
                int count = 3;
                for (int i = 107561; i <= 108321; i = i + 96) {
                    if (y < i) {
                        setDbName(type + "-" + count);
                        break;
                    }
                    count ++;
                }
            } else if (x == 18) {
                int count = 11;
                for (int i = 215125; i <= 216642; i = i + 38) {
                    if (y < i) {
                        setDbName(type + "-" + count);
                        break;
                    }
                    count ++;
                }
            }
        }
    }
}