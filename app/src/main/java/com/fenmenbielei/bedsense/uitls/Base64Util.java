package com.fenmenbielei.bedsense.uitls;

import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * IBBase64Util
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class Base64Util {
    private static final String TAG = "Base64Util";

    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
            .toCharArray();

    /**
     * Base64 encode
     *
     * @param data data
     * @return String
     */
    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;

        while (i <= end) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8)
                    | (((int) data[i + 2]) & 0x0ff);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append(legalChars[d & 63]);

            i += 3;
        }

        if (i == start + len - 2) {
            int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append(legalChars[(d >> 6) & 63]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (((int) data[i]) & 0x0ff) << 16;

            buf.append(legalChars[(d >> 18) & 63]);
            buf.append(legalChars[(d >> 12) & 63]);
            buf.append("==");
        }

        return buf.toString();
    }


    private static int decode(char c) {
        if (c >= 'A' && c <= 'Z')
            return ((int) c) - 65;
        else if (c >= 'a' && c <= 'z')
            return ((int) c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return ((int) c) - 48 + 26 + 26;
        else
            switch (c) {
                case '+':
                    return 62;
                case '/':
                    return 63;
                case '=':
                    return 0;
                default:
                    throw new RuntimeException("unexpected code: " + c);
            }
    }

    /**
     * encodeAsFile
     *
     * @param path path
     * @return Base64 String
     */
    public static String encodeAsFile(String path) {
        LogUtils.d(TAG, "[encodeAsFile], path == " + path);
        if (path.startsWith("http")) {
            return path;
        }
        try {
            StringBuffer s = new StringBuffer();
            return s.append(encode(file2byte(path))).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * encodeAsFile
     *
     * @param decodedStr str
     * @return Base64 String
     */
    public static String decodedString(String decodedStr) {
        LogUtils.d(TAG, "[decodedString], decodedStr == " + decodedStr);
        if (TextUtils.isEmpty(decodedStr)) {
            return null;
        }
        return new String(Base64.decode(decodedStr, Base64.DEFAULT));
    }


    /**
     * encodeAsFile
     *
     * @param encodedStr str
     * @return Base64 String
     */
    public static String encodedString(String encodedStr) {
        LogUtils.d(TAG, "[decodedString], str == " + encodedStr);
        if (TextUtils.isEmpty(encodedStr)) {
            return null;
        }
        return Base64.encodeToString(encodedStr.getBytes(), Base64.DEFAULT);
    }
}
