package com.example.checknut.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * TODO
 * 文件监听器
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-06 14:04
 */
@Slf4j
@Component
public class ContextFileListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private FileMonitor fileMonitor;

    @Autowired
    private FileListener fileListener;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            fileMonitor.setFileListener(fileListener);
            fileMonitor.start();
        }
    }
}
