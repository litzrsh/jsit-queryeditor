package me.litz.window;

import me.litz.util.SessionUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

    public static final String FILENAME = "./config.ini";

    private static final PropertyUtils _inst;

    static {
        _inst = new PropertyUtils();
        Properties prop = new Properties();

        String username = null;
        try {
            prop.load(new FileInputStream(FILENAME));
            // 사용자 이름을 추가 해보자
            username = prop.getProperty("username");

            // 기존 소스들
            _inst.setWidth(Integer.parseInt(prop.getProperty("width")));
            _inst.setHeight(Integer.parseInt(prop.getProperty("height")));
            _inst.setX(Integer.parseInt(prop.getProperty("x")));
            _inst.setY(Integer.parseInt(prop.getProperty("y")));
            _inst.setMaximized(Boolean.parseBoolean(prop.getProperty("maximized")));
        } catch (Throwable e) {
            _inst.setWidth(1024);
            _inst.setHeight(768);
            _inst.setX(0);
            _inst.setY(0);
            _inst.setMaximized(false);
        } finally {
            System.out.println("Username : " + username);
            SessionUtils.setUsername(username);
        }
    }

    public static int getWidth() {
        return _inst.width;
    }

    public static int getHeight() {
        return _inst.height;
    }

    public static int getX() {
        return _inst.x;
    }

    public static int getY() {
        return _inst.y;
    }

    public static boolean isMaximized() {
        return _inst.maximized;
    }

    private int width;

    private int height;

    private int x;

    private int y;

    private boolean maximized;

    protected PropertyUtils() {
        super();
        this.width = 1024;
        this.height = 768;
        this.x = 0;
        this.y = 0;
        this.maximized = false;
    }

    public void setWidth(int width) {
        if (width > 0)
            this.width = width;
        else
            this.width = 1024;
    }

    public void setHeight(int height) {
        if (height > 0)
            this.height = height;
        else
            this.height = 768;
    }

    public void setX(int x) {
        this.x = Math.max(x, 0);
    }

    public void setY(int y) {
        this.y = Math.max(y, 0);
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }
}
