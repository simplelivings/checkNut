package com.example.checknut.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    public void cc(){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,1);
        c.set(Calendar.DATE,1);
        date = c.getTime();
        date = DateUtils.setHMSOfDayToZero(date);
        System.out.println("kkkkkkkkkk" + date);
    }

    @Test
    public void dd(){
        int a = 50, b = 100;
        double c = (double)(a/b);
        System.out.println(c);
    }
}
