package com.reptile.jdfresh.jdfreshtest;

import com.bds.base.dao.MySqlDataSource;
import com.reptile.jdfresh.job.CityCode;
import com.reptile.jdfresh.job.JdFreshSourceJob;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.*;

/**
 * @author eli
 * @date 2017/11/16 14:31
 */
public class CreatTask {
    /**
     * 向总任务库添加任务
     */
    @Test
    public void test2(){
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        List<CityCode> cityCodeList = mysqlDao.query(CityCode.class, Cnd.where("status","=",1));
        List<String> urllist = new ArrayList<String>();
        urllist.add("http://list.jd.com/list.html?cat=12218,12221");
        urllist.add("http://list.jd.com/list.html?cat=12218,12222");
        urllist.add("http://list.jd.com/list.html?cat=12218,13581");
        urllist.add("http://list.jd.com/list.html?cat=12218,13591");
        urllist.add("http://list.jd.com/list.html?cat=12218,13553");
        //System.out.println("cityCodeList.size() = " + cityCodeList.size());
        for (int i=0;i<cityCodeList.size();i++){
            CityCode cityCode = cityCodeList.get(i);
            JdFreshSourceJob jdFreshSourceJob = new JdFreshSourceJob();
            jdFreshSourceJob.setPagestart(0);
            jdFreshSourceJob.setPagestop(0);
            jdFreshSourceJob.setCreate_time(new Date());
            jdFreshSourceJob.setDown_type("");
            jdFreshSourceJob.setNode("");
            jdFreshSourceJob.setProject_code("");
            jdFreshSourceJob.setStatus(777);
            jdFreshSourceJob.setWebsite("");
            jdFreshSourceJob.setTask_id(1L);
            jdFreshSourceJob.setRequest_id(20171116L);

            if (StringUtils.equals("1",cityCode.getProvinceid()) || StringUtils.equals("2",cityCode.getProvinceid())){
                for (int j=0;j<urllist.size();j++){
                    jdFreshSourceJob.setCity(cityCode.getCity());
                    jdFreshSourceJob.setProvince(cityCode.getProvince());
                    jdFreshSourceJob.setUrl(urllist.get(j));
                    mysqlDao.fastInsert(jdFreshSourceJob);

                }

            }else
            {
                for (int j=0;j<urllist.size();j++){
                    jdFreshSourceJob.setCity(cityCode.getArea());
                    jdFreshSourceJob.setProvince(cityCode.getCity());
                    jdFreshSourceJob.setUrl(urllist.get(j));
                    mysqlDao.fastInsert(jdFreshSourceJob);

                }
            }


        }


    }


    /**
     * 筛选需求和网站地址
     */
    @Test
    public void test1(){

        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");
        String province11="北京,上海,广州市,深圳市";
        String city11="东城区,西城区,朝阳区,海淀区,丰台区,石景山区,门头沟区,房山区,大兴区,房山区,燕山区,大兴区,通州区,顺义区,昌平区,平谷区,怀柔区,密云县,延庆县";
        String city12="卢湾区,黄浦区,静安区,徐汇区,虹口区,长宁路,普陀区,闸北区,杨浦区,浦东新区,宝山区,嘉定区,闵行区,松江区,青浦区,南汇区,奉贤区,金山区,崇明县";
        String city13="越秀区,荔湾区,增城区,海珠区,天河区,白云区,黄埔区,番禺区,花都区,南沙区,从化市,萝岗区";
        String city14="罗湖区,福田区,南山区,盐田区,宝安区,龙岗区";
        String [] province=province11.split(",");
        System.out.println("province = " + province.length);
        String [] beijing = city11.split(",");
        System.out.println("beijing = " + beijing.length);
        String [] shanghai = city12.split(",");
        System.out.println("shanghai = " + shanghai.length);
        String [] guangzhou = city13.split(",");
        System.out.println("guangzhou = " + guangzhou.length);
        String [] shengzheng = city14.split(",");
        System.out.println("shengzheng = " + shengzheng.length);

        Map<String,String[]> map = new HashMap<String ,String[]>();
        map.put(province[0],beijing);
        map.put(province[1],shanghai);
        map.put(province[2],guangzhou);
        map.put(province[3],shengzheng);

        Iterator<Map.Entry<String,String[]>> iterator = map.entrySet().iterator();
        while ( iterator.hasNext()){
            Map.Entry<String ,String[]> entry = iterator.next();
            String provincestr = entry.getKey();
            String citystr="";
            String [] temp = entry.getValue();
            for (int i=0;i<temp.length;i++){
                citystr = temp[i].trim();

                if (StringUtils.equals("上海",provincestr) || StringUtils.equals("北京",provincestr)) {
                    CityCode cityCode = mysqlDao.fetch(CityCode.class, Cnd.where("province", "=", provincestr).and("city", "=", citystr));
                    if (cityCode != null) {
                        mysqlDao.update(CityCode.class, Chain.make("status", 1), Cnd.where("id", "=", cityCode.getId()));
                        System.out.println("citystr = " + citystr);
                    }
                }
                else{
                    CityCode cityCode = mysqlDao.fetch(CityCode.class, Cnd.where("city", "=", provincestr).and("area", "=", citystr));
                    if (cityCode != null ) {
                        mysqlDao.update(CityCode.class, Chain.make("status", 1), Cnd.where("id", "=", cityCode.getId()));
                        System.out.println("省 : "+provincestr+"  citystr = " + citystr);
                    }
                }
            }
        }




    }



}
