package com.reptile.xiachufang.test;

import com.reptile.xiachufang.model.job.*;
import com.reptile.xiachufang.util.util.MyDao;
import org.junit.Test;
import org.nutz.dao.Dao;


public class CreateMysqlTable {
	
	@Test
	public void testCreatTable() throws Exception{
		
		Dao tdao = MyDao.getDao();

		tdao.create(XiachufangCateGorymodel.class, false);
		tdao.create(XiachufangCommentmodel.class, false);
		tdao.create(XiachufangCookbookmodel.class, false);
		tdao.create(XiachufangCookbookPagemodel.class, false);
		tdao.create(XiachufangKeywordmodel.class, false);
		tdao.create(XiachufangSearchAuthorByIdmodel.class, false);
		
	}
	
}
