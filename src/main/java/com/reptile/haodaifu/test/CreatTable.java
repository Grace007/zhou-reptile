package com.reptile.haodaifu.test;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.bds.base.dao.MySqlDataSource;
import com.reptile.haodaifu.job.*;
import com.reptile.haodaifu.model.*;
import org.junit.Test;
import org.nutz.dao.Dao;

public class CreatTable {
	@Test
	public void test() {
		Dao mysqlDao = MySqlDataSource.getMysqlDao("doctor");
		TaskToBi tobi = BIDataSource.getBiDataSource("doctor", "DOCTOR", "get_article_info");
		Dao biDao = tobi.getBiDao();
		
		mysqlDao.create(HaoDFCategoryJob.class, false);
		mysqlDao.create(HaoDFPageDoctorListJob.class, false);
		mysqlDao.create(HaoDFPageArticleListJob.class, false);
		biDao.create(HaoDFDoctorSimple.class, false);
		mysqlDao.create(HaoDFDoctorSimpleJob.class, false);
		biDao.create(HaoDFDoctorInfo.class, false);
		mysqlDao.create(HaoDFArticleSimpleJob.class, false);
		biDao.create(HaoDFArticleSimple.class, false);
		biDao.create(HaoDFArticleInfo.class, false);
		biDao.create(HaoDFArticleCommentSimple.class, false);
		
		mysqlDao.create(HaoDFWendaJob.class, false);
		mysqlDao.create(HaoDFWendaSimpleJob.class, false);
		biDao.create(HaoDFWendaInfo.class, false);
		
		mysqlDao.create(HaoDFKeywordJob.class, false);
		mysqlDao.create(HaoDFKeywordSimpleJob.class, false);
		biDao.create(HaoDFKeywordSimple.class, false);
		biDao.create(HaoDFKeywordInfo.class, false);
	}

}
