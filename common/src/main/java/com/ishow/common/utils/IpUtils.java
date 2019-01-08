package com.ishow.common.utils;

import com.ishow.common.utils.log.LogManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by menghuihui on 17/6/15.
 */

public class IpUtils {


    private static final String TAG = "IPUtils";

    private static volatile String ipAdress = null;

    public static void syncNetIp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ipAdress = getIp();
                if (ipAdress == null) {
                    ipAdress = "";
                }
                LogManager.i(TAG, "本机IP：" + ipAdress);
            }
        }).start();
    }

    public static String getIpAdress() {
        return ipAdress;
    }

    private static String getIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://ip.qq.com/");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                Pattern pattern = Pattern.compile(StringUtils.REG_IP);
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }

        } catch (Exception e) {
            if (e.getMessage() != null) {
                LogManager.e(TAG, e.getMessage());
            }
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            } catch (IOException e) {
                LogManager.e(TAG, e.getMessage());
            }
        }
        return ipLine;
    }

}
