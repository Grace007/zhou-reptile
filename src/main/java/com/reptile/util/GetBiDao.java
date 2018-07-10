package com.reptile.util;

import org.apache.commons.dbcp.BasicDataSource;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;

public class GetBiDao {

	public final static Object obj = new Object();

	public static Dao getBiData() {
		synchronized (obj) {
			Dao biDao = null ;
			try {
				BasicDataSource dataSource = new BasicDataSource();
				dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				dataSource.setUrl("jdbc:sqlserver://211.152.47.72:1433;DatabaseName=DC_DIDI_DAILY");
				dataSource.setUsername("deliverydc");
				dataSource.setPassword("delivery@dc231");
				dataSource.setTestOnBorrow(true);
				dataSource.setTestOnReturn(true);
				dataSource.setTestWhileIdle(true);
				dataSource.setValidationQuery("SELECT 1 ");
				biDao = new NutDao(dataSource);
				System.out.println("连接BI库成功");
			} catch (Exception e) {
				System.out.println(e);
			}
			return biDao;

		}
	}
	
	public static void main(String[] args) {
		getBiData();
	}

}
