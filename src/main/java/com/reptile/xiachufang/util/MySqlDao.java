package com.reptile.xiachufang.util;

import javax.sql.DataSource;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

public class MySqlDao {
	private MySqlDao(){
	}
    private static Ioc ioc;
	public static Dao getDao() {
		if(ioc==null){
			String url = "H:\\HCR\\WorkSpaceForHCR\\food\\src\\main\\java\\com\\bds\\food\\xiachufang\\util\\dao.js";
			ioc = new NutIoc(new JsonLoader(url));
		}
		DataSource ds = ioc.get(DataSource.class);
		Dao dao = new NutDao(ds);
	    return dao;
	}
}