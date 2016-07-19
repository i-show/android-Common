package com.brightyu.androidcommon;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.bright.common.BaseActivity;
import com.brightyu.androidcommon.model.Book;
import com.brightyu.androidcommon.model.TextJson;
import com.brightyu.androidcommon.model.User;

public class MainActivity extends BaseActivity {
    private static final String TAG = "nian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextJson<User> userMaker = new TextJson<>();
        userMaker.name = "This is user";
        User user = new User();
        user.userId = "001";
        user.userName = "张三";
        userMaker.addData(user);

        TextJson<Book> book = new TextJson<>();
        book.name = "This is book";
        Book childBook = new Book();
        childBook.bookId = "002";
        childBook.bookName = "太极拳";
        childBook.bookUUID = System.currentTimeMillis();
        book.addData(childBook);

        String userMakerString = JSON.toJSONString(userMaker);
        String bookMakerString = JSON.toJSONString(book);
        Log.i(TAG, "onCreate: userMakerString = " + userMakerString);
        Log.i(TAG, "onCreate: bookMakerString = " + bookMakerString);


        TextJson<User> resultUser = JSON.parseObject(userMakerString, TextJson.class);
        TextJson<Book> resultBook = JSON.parseObject(bookMakerString, new TypeReference<TextJson<Book>>() {
        });
        Log.i(TAG, "onCreate: resultUser = " + JSON.toJSONString(resultUser));
        Log.i(TAG, "onCreate: resultBook = " + JSON.toJSONString(resultBook));

        for (Book book1 : resultBook.list) {
            Log.i(TAG, "onCreate111: book1 = " + book1.toString());
        }
    }
}
