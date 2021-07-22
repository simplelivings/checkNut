package com.example.checknut.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * TODO
 *
 * @version: 1.0
 * @author: faraway
 * @date: 2021-06-23 13:30
 */

public class IOUtils {

    public static void closeIO(Closeable... closes){
        for (Closeable closeable : closes){
            try {
                if (closeable != null){
                    closeable.close();
                    System.out.println("closed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
