package com.ishow.noah.modules.main.home;

import java.util.ArrayList;
import java.util.List;

public class Test {


    private void test() {
        ArrayList<Apple> apples = new ArrayList<Apple>();
        ArrayList<? super Apple> fruits = apples;
        fruits.add(new Apple());
        apples.add(new Apple());
    }
}

