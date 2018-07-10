package com.reptile.server;

import com.bds.base.App;

public class ServerApp {

	public static void main(String[] args) throws Exception {
		 MyConfig my = new MyConfig();
		 App.start(my);
	}
}
