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

package com.ishow.noahark.modules.sample;

import android.net.Uri;

import java.util.Random;

/**
 * 测试用的
 */
public class Test {
    private static final String TAG = "Test";

    public static final String[] IMAGE_LIST = new String[]{
            "http://www.yuhaiyang.net/photoList/portfolio-1.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-2.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-3.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-4.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-5.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-6.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-7.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-8.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-9.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-10.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-11.jpg",
            "http://www.yuhaiyang.net/photoList/portfolio-12.jpg",
    };

    public static final String[] IMAGE_3 = new String[]{
            getPhoto(),
            getPhoto(),
            getPhoto(),
    };


    public static final String[] HEADER_LIST = new String[]{
            "http://www.yuhaiyang.net/header/header1.jpg",
            "http://www.yuhaiyang.net/header/header2.jpg",
            "http://www.yuhaiyang.net/header/header3.jpg",
            "http://www.yuhaiyang.net/header/header4.jpg",
            "http://www.yuhaiyang.net/header/header5.jpg",
            "http://www.yuhaiyang.net/header/header6.jpg",
            "http://www.yuhaiyang.net/header/header7.jpg",
            "http://www.yuhaiyang.net/header/header8.jpg",
            "http://www.yuhaiyang.net/header/header9.jpg",
            "http://www.yuhaiyang.net/header/header10.jpg",
            "http://www.yuhaiyang.net/header/header11.jpg",
            "http://www.yuhaiyang.net/header/header12.jpg",
            "http://www.yuhaiyang.net/header/header13.jpg",
            "http://www.yuhaiyang.net/header/header14.jpg",
            "http://www.yuhaiyang.net/header/header15.jpg",
            "http://www.yuhaiyang.net/header/header16.jpg",
            "http://www.yuhaiyang.net/header/header17.jpg",
            "http://www.yuhaiyang.net/header/header18.jpg",
    };

    public static final String[] NAME = new String[]{
            "蝴蝶为谁开",
            "打开灯光照照暖",
            "斑点水玛线",
            "情歌谱成一曲思念",
            "我只跟自己比",
            "听世界的喧闹",
            "即使我在乎",
            "孤独的路灯",
            "黛文 Make-up",
            "曾经迩的味道",
    };

    public static final String[] COMMENTS = new String[]{
            "指纹、金属机身都是别人玩剩下的了，唯一还能当噱头的是高通820",
            "玩剩下的换个名字就是黑科技了，放心雷猴不会让你失望",
            "小米没好的cpu不敢发新机，魅族确实比不上，华为就算了，925-930-935听着在不停进化，其实一点卵用都没，真耍猴。。",
            "旗舰没指纹也感叫旗舰？",
            "小米高谈爱国才是笑死人！真爱国公司注册地就不应该在开曼群岛；真爱国在印度搞发布会就不应该在地图上把藏南划给印度；真爱国货在选用天马屏时就应该大胆宣传而不是偷偷摸摸地换；真爱国民就应该诚实真诚而不是无底限地像猴子一样耍！",
            "我现在就在用，纯粹玩的东西 我是来打酱油凑字数的，麻烦大家让让……",
            "指纹和金属机身小米也会自称黑科技的，参考小米4c边缘操控",
            "你说说哪个国产机可以与Pro5平起平坐",
            "好像人家小米又要做笔记本了，魅族 华为 你怕不怕？",
            "估计指纹识别集成到屏幕按键里，完全看不见",
    };


    public static String getHeader() {
        return HEADER_LIST[new Random().nextInt(HEADER_LIST.length)];
    }

    public static Uri getHeaderUri() {
        return Uri.parse(HEADER_LIST[new Random().nextInt(HEADER_LIST.length)]);
    }

    public static String getPhoto() {
        return IMAGE_LIST[new Random().nextInt(IMAGE_LIST.length)];
    }

    public static Uri getPhotoUri() {
        return Uri.parse(IMAGE_LIST[new Random().nextInt(IMAGE_LIST.length)]);
    }

    public static String getComment() {
        return COMMENTS[new Random().nextInt(COMMENTS.length)];
    }

    public static String getName() {
        return NAME[new Random().nextInt(NAME.length)];
    }


    public static String[] getPhotos() {
        int count = new Random().nextInt(4);
        count = Math.max(1, count);
        String[] photos = new String[count];
        for (int i = 0; i < count; i++) {
            photos[i] = getPhoto();
        }
        return photos;
    }

    public static String[] getPhotos(int count) {
        count = Math.max(1, count);
        String[] photos = new String[count];
        for (int i = 0; i < count; i++) {
            photos[i] = getPhoto();
        }
        return photos;
    }

}
