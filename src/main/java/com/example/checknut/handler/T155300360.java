package com.example.checknut.handler;

import com.example.checknut.entity.ReturnInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-07-20 13:28
 */

public class T155300360 {

    @Autowired
    private static ReturnInfo returnInfo;

    /**
     * 根据相机照片，判断产品是否合格
     * 并将检测结果信息，写入全局对象returnInfo中, 方便FileListener方法调用
     * @return 合格1，不合格-1
     */
    public static int checkPart(){
//        returnInfo = new ReturnInfo();
        Map<String,Integer> nutMap = new HashMap<>();
        returnInfo.setNutMap(nutMap);
        return 1;
    };
}
