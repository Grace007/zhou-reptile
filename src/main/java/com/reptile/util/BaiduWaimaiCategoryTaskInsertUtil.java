package com.reptile.util;

import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.reptile.mobile_baidu_waimai.model.BaiduWaimaiShopInfoModel;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BaiduWaimaiCategoryTaskInsertUtil {

    public static List<BaiduWaimaiShopInfoModel> getList(Dao BiDao, int taskIdFrom, int taskIdTo, int requestId) {
        String sql = "SELECT DISTINCT city_name,keyword,task_id,request_id FROM baidu_waimai_shop_info_all WHERE task_id >= " + taskIdFrom + " AND task_id <= " + taskIdTo + " AND request_id = " + requestId;
        Sql sqlClass = Sqls.queryEntity(sql);
        sqlClass.setEntity(BiDao.getEntity(BaiduWaimaiShopInfoModel.class));
        BiDao.execute(sqlClass);
        List<BaiduWaimaiShopInfoModel> list = sqlClass.getList(BaiduWaimaiShopInfoModel.class);
        return list;
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Dao biDao = BIDataSource.getBiDataSource("baidu_waimai", "o2o", "get_o2o_info").getBiDao();
        Dao dNutDao = MySqlDataSource.getMysqlDataSource("hy_o2o").getMysqlDao();
        List<BaiduWaimaiShopInfoModel> list = getList(biDao,1,95,20180604);
        DateFormat DATA_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("category地址数量：" + list.size());
        //NutDao dNutDao = getDao();
        String sql = "INSERT INTO mobile_baidu_waimai_keyword_task(city_name,create_date,create_time,down_type,generate_category,keyword,project_code,request_id,task_id,STATUS,remark) VALUES";
        String values = "";
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            values += "('" + list.get(i).getCity_name() + "','" + DATA_FORMAT.format(new Date()) + "','" + DATA_FORMAT.format(new Date()) + "','get_o2o_info','1','" + EmojiFilterUtils.StringFilter(list.get(i).getKeyword()) + "','o2o'," + list.get(i).getRequest_id() + "," + list.get(i).getTask_id() + ",777,'_all'),";
        }
        System.out.println("拼接values耗时：" + (System.currentTimeMillis() - time1) + "毫秒");
        long t = System.currentTimeMillis();
        values = values.substring(0, values.length() - 1);
        Sql sqlClass = Sqls.queryEntity(sql + values);
        sqlClass.setEntity(dNutDao.getEntity(BaiduWaimaiShopInfoModel.class));
        dNutDao.execute(sqlClass);
        System.out.println("插入" + list.size() + "条记录耗时：" + (System.currentTimeMillis() - t) + "毫秒");
        System.out.println("done");
        System.out.println("总耗时：" + (System.currentTimeMillis() - time) + "毫秒");
    }
}
