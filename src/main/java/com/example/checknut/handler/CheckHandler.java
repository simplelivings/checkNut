package com.example.checknut.handler;

import java.util.Map;

/**
 * TODO
 * 根据零件号，调用不同零件的图片处理方法
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-14 16:05
 */

public class CheckHandler {

    /**
     * 根据零件号，调用不同零件对应的方法；
     * 由FileListener的onFileCreated方法调用
     * @param partNum
     * @return
     * @throws ClassNotFoundException
     */
    public static Map<String,Integer> process(String partNum) throws ClassNotFoundException {
        switch (partNum){
            case "T155300360":
                return T155300360.checkPart();
            default:return null;
        }
    }
}
