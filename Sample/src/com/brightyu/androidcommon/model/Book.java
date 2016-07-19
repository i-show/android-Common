/**
 * Copyright (C) 2016 yuhaiyang android source project
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

package com.brightyu.androidcommon.model;

/**
 * Created by Bright.Yu on 2016/7/15.
 */
public class Book {
    public long bookUUID;
    public String bookName;
    public String bookId;

    @Override
    public String toString() {
        return "Book{" +
                "bookUUID=" + bookUUID +
                ", bookName='" + bookName + '\'' +
                ", bookId='" + bookId + '\'' +
                '}';
    }
}
