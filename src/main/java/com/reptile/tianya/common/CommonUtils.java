package com.reptile.tianya.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommonUtils {

    private static final Logger logger = Logger.getLogger(CommonUtils.class);

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private final static DateFormat _FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static String formatYYYYMMdd(Date date) {
        if (date == null) {
            return "";
        }
        return _FORMAT.format(date);
    }

    /**
     * 输出文件
     *
     * @param file
     * @param s
     * @throws Exception
     */
    public synchronized static void toFile(String file, String s) throws Exception {
        if (s == null || s.length() == 0) {
            return;
        }
        File parent = new File(file).getParentFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                throw new RuntimeException("创建[" + parent.getAbsolutePath() + "]目录失败.");
            }
        }
        FileOutputStream fos = new FileOutputStream(file, true);
        PrintWriter pw = new PrintWriter(fos);
        // pw.append("["+getTime()+"]");
        pw.append(s + "\r\n");
        pw.flush();
        pw.close();
        fos.close();
    }

    public static String subStringJd(String begin, String end, String content) {
        if (StringUtils.isEmpty(content)) {
            return content;
        }
        if (content.indexOf(begin) == -1 || content.indexOf(end) == -1) {
            return "";
        }

        int index_s = 0;
        if (begin != null) {
            index_s = content.indexOf(begin) + begin.length();
        }
        String _end = content.substring(index_s, content.length());
        int index_e = 0;

        if (end == null) {
            index_e = content.length();
        } else {
            index_e = _end.indexOf(end);
        }
        return _end.substring(0, index_e);
    }

    public static String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; // 文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                // charset = "UTF-16LE"; // 文件编码为 Unicode
                charset = "unicode";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                // charset = "UTF-16BE"; // 文件编码为 Unicode big endian
                charset = "unicode";
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; // 文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 输出文件
     *
     * @param file
     * @throws Exception
     */
    public synchronized static void toFile(String file, String xxx, String charsetName) throws Exception {
        if (StringUtils.isEmpty(xxx)) {
            return;
        }
        FileOutputStream fos = new FileOutputStream(file, true);
        OutputStreamWriter filerWriter = new OutputStreamWriter(fos, charsetName);
        PrintWriter pw = new PrintWriter(filerWriter);
        pw.append(xxx + "\t[" + getTime() + "]");
        pw.append("\r\n");
        pw.flush();
        pw.close();
        filerWriter.close();
        fos.close();
    }

    public static String getTime() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    /**
     * http://kezhan.trip.taobao.com//hotel_detail2.htm?spm=181.7087309.0.0.
     * zHmvYd&searchBy=&shid=10048842&city=310100
     *
     * @param s
     * @param t (shid=)
     * @return 10048842
     */
    public static String parseNumber(String s, String t) {
        int index = s.indexOf(t) + t.length();
        if (index == -1) {
            return "";
        }
        String cc = "0123456789";
        String c;
        String ss = "";
        for (int i = index; i < s.length(); i++) {
            c = s.charAt(i) + "";
            if (cc.contains(c)) {
                ss = ss + c;
            } else {
                break;
            }
        }
        return ss;
    }

    public static long save(List<?> list) {
        boolean flag = false;
        StringBuffer sb;
        boolean _flag = true;
        for (Object c : list) {
            try {
                Method[] ms = c.getClass().getMethods();
                sb = new StringBuffer();
                if (_flag) {
                    StringBuffer sb1 = new StringBuffer();
                    for (Method m : ms) {
                        if (m.getName().startsWith("get") && !m.getName().equals("getClass")) {
                            sb1.append(m.getName().toString().replace("get", "") + ",");
                        }
                    }
                    CommonUtils.toFile("D:\\sale_title.txt", sb1.toString());
                    _flag = false;
                }
                for (Method m : ms) {
                    if (m.getName().startsWith("get") && !m.getName().equals("getClass")) {
                        sb.append(m.invoke(c) + ",");
                    }
                }
                logger.info(">>>>>>>>>>>>" + sb.toString());
                CommonUtils.toFile("D:\\sale.txt", sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static String md5(String source) {
        return DigestUtils.md5Hex(source);
    }

    public synchronized static void clearCookie() {
        try {
            String[] cpCmd = new String[]{"wscript", "C:\\test\\delCookie.vbs"};
            Process process = Runtime.getRuntime().exec(cpCmd);
            int val = process.waitFor();// val 是返回值
            Thread.sleep(10000);
            System.out.println("执行时间>>>" + val + "<<<ms");
        } catch (Exception e) {
            logger.error("vps is error.", e);
        }
    }

    public synchronized static void changeIp() {
        try {
            vpn();
        } catch (Exception e) {

        }

        // String LOG_FILE="c:\\test\\logs\\";
        // DateFormat FORMAT = new SimpleDateFormat("yyyyMMddHH");
        // String d = FORMAT.format(new Date());
        // String p = LOG_FILE+d+".txt";
        // File file = new File(p);
        // if(file.exists()){
        // return;
        // }
        // boolean flag = true;
        // while(flag){
        // vpn();
        // // try{
        // // String s = test();
        // // if(StringUtils.isNotEmpty(s)){
        // // flag = false;
        // // }
        // // }catch(Exception e){
        // // }
        // }
        // try{
        // file = new File(LOG_FILE);
        // File[] fs = file.listFiles();
        // for(File f : fs){
        // f.delete();
        // }
        // }catch(Exception e){
        // }
        // try {
        // CommonUtils.toFile(p,d);
        // } catch (Exception e) {
        // }

    }

    public static String test() throws Exception {
        String s = null;
        try {
            // 要想struts2的表单值天器自动填充就必须使用伪URL传参不管是使用get还是POST
            // ?path=c:/test.xml&test=2012
            String spec = "https://www.tmall.com";
            URL url = new URL(spec);
            System.out.println(url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("content-type", "text/html");

            conn.connect();// 握手
            OutputStream os = conn.getOutputStream();// 拿到输出流
            // os.write("?path=c:/test.xml&test=2012".getBytes("utf-8"));
            PrintWriter out = new PrintWriter(os);

            out.flush();
            os.flush();
            out.close();
            os.close();

            InputStream is = conn.getInputStream();// 拿到输入流
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            char[] cbuf = new char[1024];
            br.read(cbuf);
            s = new String(cbuf);
            System.out.println(s);

            br.close();
            isr.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void vpn() {
        try {
            String[] cpCmd = new String[]{"wscript", "C:\\test\\vpn.vbs"};
            Process process = Runtime.getRuntime().exec(cpCmd);
            int val = process.waitFor();// val 是返回值
            Thread.sleep(10000);
            System.out.println("执行时间>>>" + val + "<<<ms");
        } catch (Exception e) {
            logger.error("vps is error.", e);
        }
    }

    /**
     * 将带有数据中的数字提取出来 *
     */
    public static String drawNum(String str) {
        char[] nums = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        String strb = "";

        for (int j = 0; j < str.length(); j++) {
            for (char c : nums) {
                if (str.charAt(j) == c) {
                    strb += c;
                    break;
                }
            }
        }
        return strb;
    }

    /**
     * String转换为int
     */
    public static int toInt(String sourceString) {
        int result;
        try {
            result = Integer.parseInt(sourceString.trim());
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    /**
     * String转换为double
     */
    public static double toDouble(String sourceString) {
        double result;
        try {
            result = Double.parseDouble(sourceString.trim());
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    /**
     * String转换编码
     */
    public static String encodeWord(String sourceString) {
        String result;

        try {
            result = java.net.URLEncoder.encode(sourceString, "utf-8");
        } catch (Exception e) {
            result = "ERR";
        }

        return result;
    }

    /**
     * MD5转换
     */
    public static String transMD5(String pwd) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] byTemp = md5.digest(pwd.getBytes());
            StringBuffer pwdResult = new StringBuffer();
            String strTemp = "";

            for (int i = 0; i < byTemp.length; i++) {
                strTemp = (Integer.toHexString(byTemp[i] & 0XFF));
                if (strTemp.length() == 1) {
                    pwdResult.append("0");
                    pwdResult.append(strTemp);
                } else {
                    pwdResult.append(strTemp);
                }
            }

            pwd = pwdResult.toString();
            return pwd;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格林尼治时间转换
     */
    public static String gmtToDate(String sourceString, int times) {
        String result;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        switch (times) {
            case 1000:
                result = format.format(Long.parseLong(sourceString + "000"));
                break;
            default:
                result = format.format(Long.parseLong(sourceString));
                break;
        }

        return result;
    }

    /**
     * 截取字符串 0
     */
    public static String stringSub(String sourceString, String startString, String endString) {
        int start = 0, end = 0;
        String result;

        start = sourceString.indexOf(startString);
        end = sourceString.lastIndexOf(endString);
        result = sourceString.substring(start + startString.length(), end);

        return result;
    }

    /**
     * 截取字符串 1
     */
    public static String stringSub(String sourceString, int start, String endString) {
        int end = 0;
        String result;

        end = sourceString.lastIndexOf(endString);
        result = sourceString.substring(start, end);

        return result;
    }

    /**
     * 截取字符串 2
     */
    public static String stringSub(String sourceString, String startString, int end) {
        int start = 0;
        String result;

        start = sourceString.indexOf(startString);
        result = sourceString.substring(start + startString.length(), end);

        return result;
    }

    /**
     * 截取字符串 3
     */
    public static String stringSub(String sourceString, String startString) {
        int start = 0;
        String result;

        start = sourceString.indexOf(startString);
        result = sourceString.substring(start + startString.length());

        return result;
    }

    /**
     * 从字符串中提取出整数
     */
    public static String parseIntFromString(String s) {
        if (org.apache.commons.lang.StringUtils.isEmpty(s)) {
            return "";
        }
        String cc = "0123456789";
        String c;
        String ss = "";
        for (char ca : s.toCharArray()) {
            c = ca + "";
            if (cc.contains(c)) {
                ss = ss + c;
            } else {
                continue;
            }
        }
        return ss;
    }

    /**
     * 从字符串中提取出小数
     */
    public static String parseDoubleFromString(String s) {
        if (org.apache.commons.lang.StringUtils.isEmpty(s)) {
            return "";
        }
        String cc = "0123456789.";
        String c;
        String ss = "";
        for (char ca : s.toCharArray()) {
            c = ca + "";
            if (cc.contains(c)) {
                ss = ss + c;
            } else {
                continue;
            }
        }
        return ss;
    }

    /**
     * 计算 每页显示数量为定值的情况下的 页数
     */
    public static int calculatePage(int total, int limit) {
        int result;
        if (total <= limit && total > 0) {
            result = 1;
        } else {
            if (total <= 0) {
                result = -1;
            } else {
                int pageTotalTmp = total % limit;
                if (pageTotalTmp == 0) {
                    result = total / limit;
                } else {
                    result = (total - pageTotalTmp) / limit + 1;
                }
            }
        }
        return result;
    }

    /**
     * 计算 每页显示数量为定值的情况下的 页数,若大于maxPage,则设置为maxPage, 适用于最大页数有限制的网站
     */
    public static int calculatePage(int total, int limit, int maxPage) {
        int result;
        if (total <= limit && total > 0) {
            result = 1;
        } else {
            if (total <= 0) {
                result = -1;
            } else {
                int pageTotalTmp = total % limit;
                if (pageTotalTmp == 0) {
                    result = total / limit;
                } else {
                    result = (total - pageTotalTmp) / limit + 1;
                }
            }
        }

        if (result > maxPage) {
            result = maxPage;
        }
        return result;
    }

    public static void main(String args[]) throws Exception {
    }

}
