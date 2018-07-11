package com.reptile.ec.mogujie.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class FileWrite {

	public static void wirteFile(String path, String value) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					path)));

			writer.write(value);

			writer.close();

		} catch (Exception e) {

		}
	}

	public static String readFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists() || file.isDirectory())
			throw new FileNotFoundException();
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1024];
		StringBuffer sb = new StringBuffer();
		while ((fis.read(buf)) != -1) {
			sb.append(new String(buf));
			buf = new byte[1024];
		}
		return sb.toString();
	}
}
