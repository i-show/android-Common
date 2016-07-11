package com.bright.common.utils.http.okhttp.callback;


import android.util.Log;

import com.bright.common.utils.http.okhttp.OkHttpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * 下载文件的回调
 */
public abstract class FileCallBack extends CallBack<File> {
    private static final String TAG = "FileCallBack";
    /**
     * 目标文件存储的文件夹路径
     */
    private String mFilePath;
    /**
     * 目标文件存储的文件名
     */
    private String mFileName;


    public FileCallBack(String path, String name) {
        this.mFilePath = path;
        this.mFileName = name;
    }


    @Override
    public File parseNetworkResponse(Response response, int id) throws Exception {
        return saveFile(response, id);
    }


    private File saveFile(Response response, final int id) throws IOException {
        InputStream input = null;
        FileOutputStream outStream = null;
        byte[] buf = new byte[2048];
        int len = 0;
        try {
            input = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            File path = new File(mFilePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(path, mFileName);
            outStream = new FileOutputStream(file);
            while ((len = input.read(buf)) != -1) {
                sum += len;
                outStream.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        inProgress(finalSum * 1.0f / total, total, id);
                    }
                });
            }
            outStream.flush();

            return file;

        } finally {
            try {
                response.body().close();
                if (input != null) input.close();
            } catch (IOException e) {
                Log.i(TAG, "saveFile: input or response close error");
            }
            try {
                if (outStream != null) outStream.close();
            } catch (IOException e) {
                Log.i(TAG, "saveFile: outStream close error");
            }
        }
    }

}
