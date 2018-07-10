package com.reptile.util;

import com.reptile.City_code;
import org.apache.commons.dbcp.BasicDataSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.impl.NutDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class GenerateMobileBaiduWaimaiCityIdUtils {

    public static NutDao getDao() {

        long t = System.currentTimeMillis();
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://119.90.36.189:3306/buzz_o2o?useUnicode=true&characterEncoding=UTF-8");
        ds.setUsername("new_root");
        ds.setPassword("@Hyipsos");
        NutDao dNutDao = new NutDao(ds);
        System.out.println("连接数据库耗时：" + (System.currentTimeMillis() - t) + "毫秒");
        return dNutDao;
    }

    public static void generate(NutDao Dao, String path) {
        String encoding = "utf-8";
        String result = "";
        File file = new File(path);
        Long filelength = file.length();
        if (file.isFile() && file.exists()) {
            try {
                FileInputStream in = new FileInputStream(file);
                byte[] filecontent = new byte[filelength.intValue()];
                try {
                    in.read(filecontent);
                    in.close();
                    result = new String(filecontent, encoding);
                    Document doc = Jsoup.parse(result);
                    Elements citys = doc.select("li");
                    for (Element city : citys) {
                        String city_id = city.attr("data-val");
                        String city_name = city.attr("data-name");
                        //System.out.println(city_name + ":" + city_id + "\n");
                        City_code cc = new City_code();
                        cc.setCity_name(city_name);
                        cc.setCity_code(city_id);
                        cc.setWebsite("baidu");
                        try {
                            Dao.insert(cc);
                        } catch (Exception e) {

                        }
                    }
                    System.out.println("done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String path = "E:\\Users\\Kongkong\\workspace\\o2o\\src\\main\\java\\com\\bds\\o2o\\util\\baidu_city_list.txt";
        generate(getDao(), path);
    }
}
