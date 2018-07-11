package com.reptile.jdfresh.jdfreshtest;

import com.bds.base.http.HttpBase;
import com.reptile.jdfresh.job.CityCode;
import com.reptile.jdfresh.utils.MySqlDao;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eli
 * @date 2017/11/14 13:42
 */
public class CityCodeTest {

    @Test
    public void test1() throws Exception{

        Dao dao =  MySqlDao.getDao();
        dao.create(CityCode.class,false);
        File file1 = new File("F:\\HCR\\Java\\IdeaProjects\\fresh\\src\\main\\java\\com\\bds\\fresh\\utils\\areajs1.json");
        File file2 = new File("F:\\HCR\\Java\\IdeaProjects\\fresh\\src\\main\\java\\com\\bds\\fresh\\utils\\areajs2.json");
        FileInputStream fileInputStream=null;
        byte[] buffer = new byte[1024];
        fileInputStream=new FileInputStream(file1);
        int byteread = 0;
        String province="";
        String city="";
        // byteread表示一次读取到buffers中的数量。
        while ((byteread = fileInputStream.read(buffer)) != -1) {
            //System.out.write(buffer, 0, byteread);
            province+= new String(buffer, 0, byteread);
        }
        fileInputStream = new FileInputStream(file2);
        while ((byteread = fileInputStream.read(buffer))!=-1){
            city+= new String (buffer,0,byteread);
        }
        if (fileInputStream != null) {
            fileInputStream.close();
        }
        //System.out.println("city = " + city);
        //System.out.println("province = " + province);
        JSONObject provinceObject = new JSONObject(province);
        JSONObject cityObject = new JSONObject(city);
        List<CityCode> cityCodeList = new ArrayList<>();
        //System.out.println("cityObject = " + cityObject);
        for (int i=1;i<=32;i++){

            JSONObject provinceObject1 = provinceObject.getJSONObject(String.valueOf(i));
            System.out.println("provinceObject1 = " + provinceObject1);
            System.out.println("cityObject = " + cityObject.getJSONArray(String.valueOf(i)));
            JSONArray cityarray = new JSONArray(cityObject.getJSONArray(String.valueOf(i)).toString());

            System.out.println("jsoncityarray = " + cityarray);
            System.out.println("cityarray.length() = " + cityarray.length());

            String tprovince="",tcity="",tprovinceid="",tcityid="";
            tprovinceid=String.valueOf(i);
            tprovince=provinceObject1.getString("name");
            for (int j=0;j<cityarray.length();j++){
                //CityCode cityCode = new CityCode();
                tcity = cityarray.getJSONObject(j).getString("name");
                tcityid = cityarray.getJSONObject(j).getString("id");
                System.out.println("###############");
                System.out.println("tprovince = " + tprovince);
                System.out.println("tprovinceid = " + tprovinceid);
                System.out.println("tcity = " + tcity);
                System.out.println("tcityid = " + tcityid);

                String url ="http://d.jd.com/area/get?fid="+tcityid;
                String html ="";
                try {
                    html = HttpBase.get(url,"utf8").getResult();
                    JSONArray areaarray = new JSONArray(html);
                    System.out.println("areaarray = " + areaarray);
                    for (int k=0;k<areaarray.length();k++){
                        CityCode cityCode = new CityCode();
                        String area=areaarray.getJSONObject(k).getString("name");
                        String areaid=areaarray.getJSONObject(k).getString("id");
                        cityCode.setCity(tcity);
                        cityCode.setStatus(0);
                        cityCode.setArea(area);
                        cityCode.setAreaid(areaid);
                        cityCode.setCityid(tcityid);
                        cityCode.setLastid("0");
                        cityCode.setProvince(tprovince);
                        cityCode.setProvinceid(tprovinceid);
                        try{
                            dao.insert(cityCode);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

    }
    @Test
    public void test2() throws Exception{

        Dao dao =  MySqlDao.getDao();
        dao.create(CityCode.class,false);
        File file1 = new File("F:\\HCR\\Java\\IdeaProjects\\fresh\\src\\main\\java\\com\\bds\\fresh\\utils\\areajs1.json");
        File file2 = new File("F:\\HCR\\Java\\IdeaProjects\\fresh\\src\\main\\java\\com\\bds\\fresh\\utils\\areajs2.json");
        FileInputStream fileInputStream=null;
        byte[] buffer = new byte[1024];
        fileInputStream=new FileInputStream(file1);
        int byteread = 0;
        String province="";
        String city="";
        // byteread表示一次读取到buffers中的数量。
        while ((byteread = fileInputStream.read(buffer)) != -1) {
            //System.out.write(buffer, 0, byteread);
            province+= new String(buffer, 0, byteread);
        }
        fileInputStream = new FileInputStream(file2);
        while ((byteread = fileInputStream.read(buffer))!=-1){
            city+= new String (buffer,0,byteread);
        }
        if (fileInputStream != null) {
            fileInputStream.close();
        }
        //System.out.println("city = " + city);
        //System.out.println("province = " + province);
        JSONObject provinceObject = new JSONObject(province);
        JSONObject cityObject = new JSONObject(city);
        List<CityCode> cityCodeList = new ArrayList<>();
        //System.out.println("cityObject = " + cityObject);
        for (int i=1;i<=30;i++){

            JSONObject provinceObject1 = provinceObject.getJSONObject(String.valueOf(i));
            System.out.println("provinceObject1 = " + provinceObject1);
            System.out.println("cityObject = " + cityObject.getJSONArray(String.valueOf(i)));
            JSONArray cityarray = new JSONArray(cityObject.getJSONArray(String.valueOf(i)).toString());

            System.out.println("jsoncityarray = " + cityarray);
            System.out.println("cityarray.length() = " + cityarray.length());

            String tprovince="",tcity="",tprovinceid="",tcityid="";
            tprovinceid=String.valueOf(i);
            tprovince=provinceObject1.getString("name");
            for (int j=0;j<cityarray.length();j++){
                //CityCode cityCode = new CityCode();
                tcity = cityarray.getJSONObject(j).getString("name");
                tcityid = cityarray.getJSONObject(j).getString("id");
                System.out.println("###############");
                System.out.println("tprovince = " + tprovince);
                System.out.println("tprovinceid = " + tprovinceid);
                System.out.println("tcity = " + tcity);
                System.out.println("tcityid = " + tcityid);

                String url ="http://d.jd.com/area/get?fid="+tcityid;
                String html ="";
                try {
                    html = HttpBase.get(url,"utf8").getResult();
                    JSONArray areaarray = new JSONArray(html);
                    System.out.println("areaarray = " + areaarray);
                    for (int k=0;k<areaarray.length();k++){
                        CityCode cityCode = new CityCode();
                        String area=areaarray.getJSONObject(k).getString("name");
                        String areaid=areaarray.getJSONObject(k).getString("id");
                        cityCode.setCity(tcity);
                        cityCode.setStatus(0);
                        cityCode.setArea(area);
                        cityCode.setAreaid(areaid);
                        cityCode.setCityid(tcityid);
                        cityCode.setLastid("0");
                        cityCode.setProvince(tprovince);
                        cityCode.setProvinceid(tprovinceid);
                        try{
                            dao.insert(cityCode);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }

    }

    /**
     * 获取地址current_areaid
     */
    @Test
    public void test3(){
        Dao mysqldao = MySqlDao.getDao();
        List<CityCode> cityCodeList = mysqldao.query(CityCode.class, Cnd.where("status","=",0));
        System.out.println("cityCodeList.size() = " + cityCodeList.size());
        for (int i=0;i<cityCodeList.size();i++){
            CityCode cityCode = cityCodeList.get(i);
            String current_areaid=cityCode.getProvinceid()+"-"+cityCode.getCityid()+"-"+cityCode.getAreaid()+"-"+"0";
            System.out.println("current_areaid = " + current_areaid);
            mysqldao.update(CityCode.class, Chain.make("current_areaid",current_areaid), Cnd.where("id","=",cityCode.getId()));
        }

    }

    @Test
    public void test4(){
        Dao mysqldao = MySqlDao.getDao();
        List<CityCode> cityCodeList = mysqldao.query(CityCode.class, Cnd.where("status","=",0));
        System.out.println("cityCodeList.size() = " + cityCodeList.size());
        for (int i=0;i<cityCodeList.size();i++){
            CityCode cityCode = cityCodeList.get(i);
            String current_areaid=cityCode.getProvinceid()+"-"+cityCode.getCityid()+"-"+cityCode.getAreaid()+"-"+"0";
            System.out.println("current_areaid = " + current_areaid);
            mysqldao.update(CityCode.class, Chain.make("current_areaid",current_areaid), Cnd.where("id","=",cityCode.getId()));
        }

    }



}
