package com.reptile.mobile_eleme.test;

import com.reptile.mobile_eleme.model.ElemeProductInfoModel;
import org.nutz.dao.TableName;
import org.nutz.dao.impl.NutDao;

import java.util.List;
import java.util.concurrent.CountDownLatch;


public class ProductInsertThread extends Thread {
    private CountDownLatch cdl;
    private String name;
    private NutDao dNutDao;
    private List<ElemeProductInfoModel> list;

    public ProductInsertThread(CountDownLatch cdl, String name, NutDao dNutDao, List<ElemeProductInfoModel> list) {
        this.cdl = cdl;
        this.name = name;
        this.dNutDao = dNutDao;
        this.list = list;
    }

    public void run() {
        System.out.print("线程:" + name + "开始执行");
        //for (ElemeProductInfoModel model : list) {
        long product_time = System.currentTimeMillis();
        try {
            TableName.set(null);
            dNutDao.fastInsert(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            TableName.clear();
        }
        System.out.print("插入一条数据耗时:" +
                (System.currentTimeMillis() - product_time) + "\n");
        //}
        cdl.countDown();
        System.out.print("线程:" + name + "执行完成");
    }
}
