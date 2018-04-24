package com.example.dblib.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FilePath {

    public static final int TYPE_FILE = 0;
    public static final int TYPE_DIR  = 1;

    public String name;

    /**
     * 当前的绝对路径
     */
    public String path;

    /**
     * 父路径
     */
    public String parentalPath;
    /**
     * 文件类型,
     */
    public int isDir;

    /**
     * last modified time
     */
    public long last_md;
    @Generated(hash = 1500147681)
    public FilePath(String name, String path, String parentalPath, int isDir,
            long last_md) {
        this.name = name;
        this.path = path;
        this.parentalPath = parentalPath;
        this.isDir = isDir;
        this.last_md = last_md;
    }
    @Generated(hash = 1343939652)
    public FilePath() {
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getParentalPath() {
        return this.parentalPath;
    }
    public void setParentalPath(String parentalPath) {
        this.parentalPath = parentalPath;
    }
    public int getIsDir() {
        return this.isDir;
    }
    public void setIsDir(int isDir) {
        this.isDir = isDir;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getLast_md() {
        return this.last_md;
    }
    public void setLast_md(long last_md) {
        this.last_md = last_md;
    }

    @Override
    public String toString() {
        return "FilePath{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", parentalPath='" + parentalPath + '\'' +
                ", isDir=" + isDir +
                ", last_md=" + last_md +
                '}';
    }
}
