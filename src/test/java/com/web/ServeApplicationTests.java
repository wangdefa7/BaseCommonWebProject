package com.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServeApplicationTests {

    @Test
    public void contextLoads() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(dateFormat.format(new Date(Long.MAX_VALUE)));
    }

}
