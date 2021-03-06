package com.fenmenbielei.bedsense.uitls;

import java.security.MessageDigest;

/**
 * Created by yanjunhui
 * on 2016/9/24.
 * email:303767416@qq.com
 */
public class MD5Utils {

    public static String createMD5(String text) {
        return createMD5(text, 32);
    }

    public static String createMD5In16(String text) {
        return createMD5(text, 16);
    }

    private static String createMD5(String plainText, int byteLength) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            if (byteLength == 16) {
                return buf.toString().substring(8, 24);
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
