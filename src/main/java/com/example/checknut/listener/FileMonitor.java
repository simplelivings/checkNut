package com.example.checknut.listener;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * TODO
 * 文件监听器，管理类
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-06 14:03
 */
public class FileMonitor {
    private FileListener fileListener;  //监听事件
    private String path;    //监听的文件路径
    private FileAlterationMonitor monitor;

    //不能由配置文件注入
    private long defaultInterval = 1000L;  //默认监听的时间间隔

    public FileMonitor() {
    }

    public FileMonitor(String path) {
        this.path = path;
        this.monitor = new FileAlterationMonitor(defaultInterval);
    }

    public FileMonitor(String path, long interval) {
        this.path = path;
        this.monitor = new FileAlterationMonitor(interval);
    }

    public void setFileListener(FileListener fileListener) {
        this.fileListener = fileListener;
    }

    public void start() {
        if (monitor == null) {
            throw new IllegalStateException("Listener must not be null");
        }
        if (path == null) {
            throw new IllegalStateException("Listen path must not be null");
        }

        FileAlterationObserver observer = new FileAlterationObserver(path);
        observer.addListener(fileListener);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
