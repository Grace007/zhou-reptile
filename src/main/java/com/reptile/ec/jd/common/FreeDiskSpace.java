package com.reptile.ec.jd.common;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;

public class FreeDiskSpace {
	private static Logger logger = Logger.getLogger(FreeDiskSpace.class);

	public static String getCurrentPath() {

		String current_path = System.getProperty("user.dir");
		if (StringUtils.isEmpty(current_path)) {
			File directory = new File("");
			try {
				current_path = directory.getCanonicalPath();
			} catch (Exception e) {
			}
			if (current_path.endsWith("bin")) {
				current_path = current_path.substring(0, current_path.length() - 4);
			}
		} else {
			if (current_path.endsWith("bin")) {
				current_path = current_path.substring(0, current_path.length() - 4);
			}
		}

		System.out.println("current path is:" + current_path);

		return current_path;
	}

	public Boolean freeDiskSpaceRemain(String current_path) {
		Boolean flag = false;
		String diskName;
		try {
			diskName = current_path.split(":")[0];
		} catch (Exception e) {
			diskName = null;
		}
		if (diskName != null) {
			File file = new File(diskName + ":");
			long totalSpace = file.getTotalSpace();
			long freeSpace = file.getFreeSpace();
			long usedSpace = totalSpace - freeSpace;

//			System.out.println(diskName + "盘总空间大小 : " + totalSpace / 1024 / 1024 + "MB");
			logger.info(diskName + "盘剩余空间大小 : " + freeSpace / 1024 / 1024 + "MB");
//			System.out.println(diskName + "盘剩余空间大小 : " + freeSpace / 1024 / 1024 + "MB");
//			System.out.println(diskName + "盘已用空间大小 : " + usedSpace / 1024 / 1024 + "MB");

			if (freeSpace / 1024 / 1024 >= 200) {
				flag = true;
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		FreeDiskSpace test = new FreeDiskSpace();
		test.freeDiskSpaceRemain(test.getCurrentPath());
	}
}
