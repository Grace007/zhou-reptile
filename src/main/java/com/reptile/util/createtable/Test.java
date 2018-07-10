package com.reptile.util.createtable;

import org.nutz.dao.impl.NutDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Test {

    public static void main(String[] args) {

        ApplicationContext ctx = new FileSystemXmlApplicationContext("src\\main\\java\\com\\reptile\\util\\createtable\\dataSource.xml");

        NutDao dao = (NutDao) ctx.getBean("tDao");

        System.out.println("==================================");

//        dao.create(ElemeBreakfastKeywordJob.class, false);
//        dao.create(ElemeBreakfastKeywordNextJob.class, false);
        //dao.create(ElemeCommentTaskJob.class, false);
        //dao.create(ElemeCommentDailyTaskJob.class, false);

        System.out.println("+++++++++++++++++++++++++++++++++++");

    }

}