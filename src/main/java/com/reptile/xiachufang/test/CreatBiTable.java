package com.reptile.xiachufang.test;

import com.bds.api.TaskToBi;
import com.bds.base.dao.BIDataSource;
import com.reptile.xiachufang.model.*;
import org.junit.Test;
import org.nutz.dao.Dao;


public class CreatBiTable {
	/*@Test
	public void testCreatTable() throws Exception{
		Dao tdao = MyDao.getDao();
		tdao.create(XiachufangAuthorCookbookSimple.class, false);
		tdao.create(XiachufangAuthorInfo.class, false);
		tdao.create(XiachufangCateGorySimple.class, false);
		tdao.create(XiachufangCommentSimple.class, false);
		tdao.create(XiachufangCookbookInfo.class, false);
		tdao.create(XiachufangKeywordSimple.class, false);
	}*/
	@Test
	public void testBICreatTable() throws Exception{
		TaskToBi tobi = BIDataSource.getBiDataSource("xiachufang","FOOD", "get_recipe_info");
		Dao  biDao = tobi.getBiDao();
		biDao.create(XiachufangAuthorCookbookSimple.class, false);
		biDao.create(XiachufangAuthorInfo.class, false);
		biDao.create(XiachufangCateGorySimple.class, false);
		biDao.create(XiachufangCommentSimple.class, false);
		biDao.create(XiachufangCookbookInfo.class, false);
		biDao.create(XiachufangKeywordSimple.class, false);
	}
}
