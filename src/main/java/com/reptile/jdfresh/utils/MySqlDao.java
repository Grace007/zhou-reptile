package com.reptile.jdfresh.utils;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;

import javax.sql.DataSource;

public class MySqlDao {
	private MySqlDao(){
	}
    private static Ioc ioc;
	public static Dao getDao() {
		if(ioc==null){
			String url = "F:\\HCR\\Java\\IdeaProjects\\fresh\\src\\main\\java\\com\\bds\\fresh\\jdfresh\\utils\\dao.js";
			ioc = new NutIoc(new JsonLoader(url));
		}
		DataSource ds = ioc.get(DataSource.class);
		Dao dao = new NutDao(ds);
	    return dao;
	}
}