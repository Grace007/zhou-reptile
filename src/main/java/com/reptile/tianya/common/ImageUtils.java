package com.reptile.tianya.common;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageUtils {

	public static void main(String[] args) {
		ImageUtils e = new ImageUtils();
		
		try {
//			e.test1();
			e.test2();
//			e.test3();
//			e.test4();
//			e.test5();
//			e.test6();
//			e.test7();
//			e.test8();
//			e.test9();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	//指定大小进行缩放
	private void test1() throws Exception {
		//size(宽度, 高度)
		
        /*  
         * 若图片横比200小，高比300小，不变  
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变  
         * 若图片横比200大，高比300小，横缩小到200，图片比例不变  
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300  
         */ 
		Thumbnails.of("images/a380_1280x1024.jpg") 
		        .size(200, 300)
		        .toFile("f:/a380_200x300.jpg");
		
		Thumbnails.of("images/a380_1280x1024.jpg") 
		        .size(2560, 2048) 
		        .toFile("f:/a380_2560x2048.jpg");
	}
	
	//按照比例进行缩放
	private void test2() throws Exception {
		//scale(比例)
//		Thumbnails.of("images/a380_1280x1024.jpg") 
//		        .scale(0.25f)
//		        .toFile("f:/a380_25%.jpg");
//		
		Thumbnails.of("images/a380_1280x1024.jpg") 
		        .scale(15f)
		        .toFile("f:/a380_500%.png");
	}
	
	//不按照比例，指定大小进行缩放
	private void test3() throws Exception {
		//keepAspectRatio(false) 默认是按照比例缩放的
		Thumbnails.of("images/a380_1280x1024.jpg") 
		        .size(200, 200) 
                .keepAspectRatio(false) 
		        .toFile("f:/a380_200x200.jpg");
	}
	
	//旋转
	private void test4() throws Exception {
		//rotate(角度),正数：顺时针 负数：逆时针
		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
		        .rotate(90) 
		        .toFile("f:/a380_rotate+90.jpg"); 

		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
		        .rotate(-90) 
		        .toFile("f:/a380_rotate-90.jpg"); 
	}
	
	//水印
	private void test5() throws Exception {
		//watermark(位置，水印图，透明度)
		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
		        .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f) 
                .outputQuality(0.8f) 
		        .toFile("f:/a380_watermark_bottom_right.jpg");
		
		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
		        .watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f) 
		        .outputQuality(0.8f) 
		        .toFile("f:/a380_watermark_center.jpg");
	}
	
	//裁剪
	private void test6() throws Exception {
		//sourceRegion()
		
		//图片中心400*400的区域
		Thumbnails.of("images/a380_1280x1024.jpg")
				.sourceRegion(Positions.CENTER, 400,400)
				.size(200, 200)
                .keepAspectRatio(false) 
		        .toFile("f:/a380_region_center.jpg");
		
		//图片右下400*400的区域
		Thumbnails.of("images/a380_1280x1024.jpg")
				.sourceRegion(Positions.BOTTOM_RIGHT, 400,400)
				.size(200, 200)
                .keepAspectRatio(false) 
		        .toFile("f:/a380_region_bootom_right.jpg");
		
		//指定坐标
		Thumbnails.of("images/a380_1280x1024.jpg")
				.sourceRegion(600, 500, 400, 400)
				.size(200, 200)
		        .keepAspectRatio(false) 
		        .toFile("f:/a380_region_coord.jpg");
	}
	
	//转化图像格式
	private void test7() throws Exception {
		//outputFormat(图像格式)
		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
                .outputFormat("png") 
		        .toFile("f:/a380_1280x1024.png"); 
		
		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
		        .outputFormat("gif") 
		        .toFile("f:/a380_1280x1024.gif"); 
	}
	
	//输出到OutputStream
	private void test8() throws Exception {
		//toOutputStream(流对象)
		OutputStream os = new FileOutputStream("f:/a380_1280x1024_OutputStream.png");
		Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
                .toOutputStream(os);
	}
	
	//输出到BufferedImage
	private void test9() throws Exception {
		//asBufferedImage() 返回BufferedImage
		BufferedImage thumbnail = Thumbnails.of("images/a380_1280x1024.jpg") 
				.size(1280, 1024)
				.asBufferedImage();
		ImageIO.write(thumbnail, "jpg", new File("f:/a380_1280x1024_BufferedImage.jpg")); 
	}


}
