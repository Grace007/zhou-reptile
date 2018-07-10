package com.reptile.util.WaimaiNodesDivideUtils.common;

import com.bds.base.util.SpringContextHolder;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.SqlExpressionGroup;

import java.util.List;

public class UpdateNodeUtils {

	private static Dao tDao = SpringContextHolder.getBean("tDao");
	private static Logger logger = Logger.getLogger(UpdateNodeUtils.class);

	public static void main(String[] args) {

	}

	public static int getRemaining(String website, long task_id, long request_id, Class<Object> off) {
		long time = System.currentTimeMillis();
		int remaining = 0;
		SqlExpressionGroup e1 = Cnd.exps("status", "=", 0).or("status", "=", 1).or("status", "=", 888);
		List<Object> list = tDao.query(off,
				Cnd.where("request_id", "=", request_id).and("task_id", "=", task_id).and(e1));
		remaining = list.size();
		logger.info("剩余任务数量：" + remaining + "，耗时：" + (System.currentTimeMillis() - time));

		return remaining;
	}

}
