package com.zml.acl.controller;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-16 8:37
 */
public class T {
    public static void main(String[] args) {
//        实现一个二分查找的方法

        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int target = 9;
        int left = 0;

        int right = arr.length - 1;
        int mid = (left + right) / 2;

        while (left <= right) {
            if (arr[mid] == target) {
                System.out.println(mid);
                break;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
            mid = (left + right) / 2;

//        二分查找 的实现

        }
    }
}
