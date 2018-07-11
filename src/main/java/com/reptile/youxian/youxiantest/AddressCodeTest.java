package com.reptile.youxian.youxiantest;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.http.HttpsBase;
import com.reptile.youxian.job.YouXianCategoryJob;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.nutz.dao.Dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author eli
 * @date 2017/11/22 11:25
 */
public class AddressCodeTest {
    @Test
    public void test1() throws Exception{
        Dao mysqlDao = MySqlDataSource.getMysqlDao("fresh");

        String city11="北京,上海,广州,深圳";
        String area11="东城区,西城区,朝阳区,海淀区,丰台区,石景山区,门头沟区,房山区,大兴区,燕山区,大兴区,通州区,顺义区,昌平区,平谷区,怀柔区,密云县,延庆县";
        String area12="卢湾区,黄浦区,静安区,徐汇区,虹口区,长宁区,普陀区,闸北区,杨浦区,浦东新区,宝山区,嘉定区,闵行区,松江区,青浦区,南汇区,奉贤区,金山区,崇明县";
        String area13="越秀区,荔湾区,增城区,海珠区,天河区,白云区,黄埔区,番禺区,花都区,南沙区,从化市,萝岗区";
        String area14="罗湖区,福田区,南山区,盐田区,宝安区,龙岗区";
        String [] city=city11.split(",");
        System.out.println("city = " + city.length);
        String [] beijing = area11.split(",");
        System.out.println("beijing = " + beijing.length);
        String [] shanghai = area12.split(",");
        System.out.println("shanghai = " + shanghai.length);
        String [] guangzhou = area13.split(",");
        System.out.println("guangzhou = " + guangzhou.length);
        String [] shengzheng = area14.split(",");
        System.out.println("shengzheng = " + shengzheng.length);

        String [] category={"水果","蔬菜","肉蛋","水产"};

        Map<String,String[]> map = new HashMap<String ,String[]>();
        map.put(city[0],beijing);
        map.put(city[1],shanghai);
        map.put(city[2],guangzhou);
        map.put(city[3],shengzheng);

        Iterator<Map.Entry<String,String[]>> iterator = map.entrySet().iterator();

        while ( iterator.hasNext()){
            Map.Entry<String ,String[]> entry = iterator.next();
            String citystr = entry.getKey();
            String areastr="";
            String [] temp = entry.getValue();
            for (int i=0;i<temp.length;i++){
                areastr=temp[i];
                String url = "https://as-vip.missfresh.cn/web20/address/place/suggestion?key=IBKBZ-EWKH4-AZZUL-DAOBQ-HBWP2-JJFD7&keyword="+java.net.URLEncoder.encode(temp[i], "utf8")+"&platform=web&region="+java.net.URLEncoder.encode(citystr, "utf8");
                String  html = HttpsBase.get(url,"utf-8").getResult();

                JSONArray addressarray = new JSONObject(html).getJSONArray("data");
                String addresscode="";
                for (int j=0;j<addressarray.length();j++){
                    addresscode = addressarray.getJSONObject(j).getString("adcode");
                    break;
                }

                System.out.println("###########");
                System.out.println(citystr+"   "+temp[i]);
                System.out.println("html = " + html);
                System.out.println("addresscode = " + addresscode);

                for (int k=0;k<category.length;k++){
                    String categorystr = category[k];
                    String categoryurl="";
                    if (StringUtils.equals(citystr,"北京") || StringUtils.equals(citystr,"上海") ){
                    if (StringUtils.equals(categorystr,"水果")){
                        categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hd-fruit";

                    }else if (StringUtils.equals(categorystr,"蔬菜")){
                        categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hd-sc";
                    }
                    else if (StringUtils.equals(categorystr,"肉蛋")){
                        categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hd-food";
                    }
                    else if (StringUtils.equals(categorystr,"水产")){
                        categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hd-seafood";
                    }
                    else{
                        categoryurl="";
                    }
                    }else{
                        if (StringUtils.equals(categorystr,"水果")){
                            categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hn-fruit";

                        }else if (StringUtils.equals(categorystr,"蔬菜")){
                            categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hn-sc";
                        }
                        else if (StringUtils.equals(categorystr,"肉蛋")){
                            categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hn-food";
                        }
                        else if (StringUtils.equals(categorystr,"水产")){
                            categoryurl="https://as-vip.missfresh.cn/v3/product/category/jsd-hn-seafood";
                        }
                        else{
                            categoryurl="";
                        }
                    }

                    YouXianCategoryJob youXianCategoryJob = new YouXianCategoryJob();
                    youXianCategoryJob.setCategory(categorystr);
                    youXianCategoryJob.setCity(citystr);
                    youXianCategoryJob.setUrl(categoryurl);
                    youXianCategoryJob.setCreate_time(new Date());
                    youXianCategoryJob.setDown_type("get_sort_info");
                    youXianCategoryJob.setProject_code("DC_FRESH");
                    youXianCategoryJob.setRequest_id(20171122L);
                    youXianCategoryJob.setStatus(777);
                    youXianCategoryJob.setTask_id(1L);
                    youXianCategoryJob.setWebsite("fresh");
                    youXianCategoryJob.setArea(areastr);
                    youXianCategoryJob.setAreaid(addresscode);

                    System.out.println("youXianCategoryJob.getCity() = " + youXianCategoryJob.getCity());
                    System.out.println("youXianCategoryJob = " + youXianCategoryJob.getCategory());
                    System.out.println("youXianCategoryJob = " + youXianCategoryJob.getUrl());
                    System.out.println("youXianCategoryJob = " + youXianCategoryJob.getRequest_id());
                    System.out.println("youXianCategoryJob = " + youXianCategoryJob.getTask_id());
                    System.out.println("youXianCategoryJob = " + youXianCategoryJob.getArea());
                    System.out.println("youXianCategoryJob = " + youXianCategoryJob.getAreaid());

                    try {
                        mysqlDao.fastInsert(youXianCategoryJob);
                    }catch (Exception e){
                        System.out.println("插入失败.." + e);
                    }
                }

            }
        }







    }
}
