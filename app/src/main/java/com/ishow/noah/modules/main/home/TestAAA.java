package com.ishow.noah.modules.main.home;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class TestAAA {

    public int[] twoSum(int[] nums, int target) {
        // int size = nums.length;
        // for (int i = 0; i< size ;i ++ ) {
        //     for (int j = size -1 ;j > i ;j--) {
        //         if(nums[i] + nums[j] == target) {
        //             return new int[]{i, j};
        //         }
        //     }
        // }
        // return new int[]{99,99};
        int size = nums.length;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (map.containsKey(target - nums[i])) {
                return new int[]{map.get(target - nums[i]), i};
            }
            map.put(nums[i], i);
        }

        return new int[]{99, 99};
    }

    public static int reverse(int x) {
        long n = 0;
        while (x != 0) {
            n = n * 10 + x % 10;
            x = x / 10;
        }
        return (int) n == n ? (int) n : 0;
    }

    public static int missingNumber(int[] nums) {
        int length = nums.length;
        int sum = (length * (length + 1)) / 2;
        for (int i = 0; i <= length - 1; i++) {
            sum = sum - nums[i];
        }
        return sum;
    }
}
