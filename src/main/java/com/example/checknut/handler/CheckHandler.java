package com.example.checknut.handler;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-14 16:05
 */

public class CheckHandler {

    /**
     * 根据零件号，调用不同零件对应的方法；
     * @param partNum
     * @return
     * @throws ClassNotFoundException
     */
    public static int process(String partNum) throws ClassNotFoundException {
        switch (partNum){
            case "T155300360":
                return T155300360.checkPart();
            default:return 0;
        }
    }
}
