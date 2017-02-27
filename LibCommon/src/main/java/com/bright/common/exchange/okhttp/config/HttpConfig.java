/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.exchange.okhttp.config;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Http的配置
 */

public class HttpConfig {
    /**
     * 默认的回调时间
     */
    public static final long DEFAULT_TIME_OUT_MILLISECONDS = 10_000L;

    /**
     * Request readTimeOut
     */
    private long readTimeOut;
    /**
     * Request writeTimeOut
     */
    private long writeTimeOut;
    /**
     * Request connTimeOut
     */
    private long connTimeOut;


    public long getReadTimeOut() {
        if (readTimeOut <= 0) {
            readTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        }
        return readTimeOut;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public long getWriteTimeOut() {
        if (writeTimeOut <= 0) {
            writeTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        }
        return writeTimeOut;
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public long getConnTimeOut() {
        if (connTimeOut <= 0) {
            connTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        }
        return connTimeOut;
    }

    public void setConnTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
    }
}
