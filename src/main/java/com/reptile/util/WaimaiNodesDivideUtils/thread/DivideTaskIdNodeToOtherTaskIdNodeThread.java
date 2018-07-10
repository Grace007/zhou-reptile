package com.reptile.util.WaimaiNodesDivideUtils.thread;

import com.bds.base.dao.MySqlDataSource;
import com.bds.base.util.Constant;
import com.reptile.util.WaimaiNodesDivideUtils.job.Nodes;
import com.reptile.util.WaimaiNodesDivideUtils.job.Waimai_task_list;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

import java.util.List;

public class DivideTaskIdNodeToOtherTaskIdNodeThread extends Thread {
	//private Dao tDao = SpringContextHolder.getBean("tDao");
    private Dao tDao = MySqlDataSource.getMysqlDataSource(Constant.ZNode).getMysqlDao();
	private static Logger logger = Logger.getLogger(DivideTaskIdNodeToOtherTaskIdNodeThread.class);
	private Waimai_task_list task;

	public DivideTaskIdNodeToOtherTaskIdNodeThread(Waimai_task_list task) {
		this.task = task;
	}

	public void run() {
		try {
			tDao.update(Waimai_task_list.class, Chain.make("divide_task_or_not", 1),
					Cnd.where("id", "=", task.getId()));
			
			if (StringUtils.isEmpty(task.getTask_to_task())) {
				logger.info("此任务存在问题，请正确设置任务");
				tDao.update(Waimai_task_list.class, Chain.make("divide_task_or_not", 404),
						Cnd.where("id", "=", task.getId()));
			}
			String tasks[] = task.getTask_to_task().split(",");
			long taskOne = Long.parseLong(tasks[0]);
			long taskTwo = Long.parseLong(tasks[1]);

			/*先停掉meituan_waimai_list_task里面的任务**/
//			tDao.update(MobileMeituanWaimaiListTaskModel.class, Chain.make("node", "ting"),
//					Cnd.where("request_id", "=", task.getRequest_id())
//					.and("task_id", "=", taskOne).and("WheatherCategory", "=", task.getDescription()));
			if (taskTwo != task.getTask_id()) {
				logger.info("此任务存在问题，请正确设置任务");
				tDao.update(Waimai_task_list.class, Chain.make("divide_task_or_not", 404),
						Cnd.where("id", "=", task.getId()));
			}
			/* 查询需要跟新的list **/
			List<Nodes> nodeLists = tDao.query(Nodes.class, Cnd.where("task_id", "=", taskTwo));
			/* 查询原来task所用的节点 **/
			List<Nodes> nodes = tDao.query(Nodes.class, Cnd.where("task_id", "=", taskOne));
			for (int i = 0; i < nodes.size(); i++) {
				tDao.update(Nodes.class,
						Chain.make("request_id", nodes.get(i).getRequest_id())
						.add("node", nodes.get(i).getNode())
						.add("description", nodes.get(i).getDescription())
						.add("remark", nodes.get(i).getRemark()),
						Cnd.where("id", "=", nodeLists.get(i).getId()));
			}
			
			List<Waimai_task_list> waimai = tDao.query(Waimai_task_list.class, Cnd.where("task_id", "=", taskOne));
			
			tDao.update(Waimai_task_list.class, Chain.make("divide_task_or_not", 2)
					.add("request_id", waimai.get(0).getRequest_id())
					.add("divide_waimailist_or_not", 0)
					.add("description", waimai.get(0).getDescription())
					.add("nodes", null),
					Cnd.where("id", "=", task.getId()));
			logger.info("task_id为" + taskOne + "的节点已经全部分配给task_id为" + taskTwo + "的抓取任务了");
		} catch (Exception e) {
			tDao.update(Waimai_task_list.class, Chain.make("divide_task_or_not", 3),
					Cnd.where("id", "=", task.getId()));
			logger.info("根据taskid来重新分配节点失败：" + e);
		}
	}
}
