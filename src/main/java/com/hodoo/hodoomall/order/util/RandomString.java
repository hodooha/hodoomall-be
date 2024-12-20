package com.hodoo.hodoomall.order.util;

import java.security.SecureRandom;

public class RandomString {
    /**
     * 자릿수(digit) 만큼 랜덤한 숫자를 반환 받습니다.
     *
     * @param length 자릿수
     * @return 랜덤한 숫자
     */
    public static int generateRandomNum(int length) {
        SecureRandom secureRandom = new SecureRandom();
        int upperLimit = (int) Math.pow(10, length);
        return secureRandom.nextInt(upperLimit);
    }

    /**
     * 시작 범위(start)와 종료 범위(end) 값을 받아서 랜덤한 숫자를 반환 받습니다.
     *
     * @param start 시작 범위
     * @param end   종료 범위
     * @return 랜덤한 숫자
     */
    public static int generateRangeRandomNum(int start, int end) {
        SecureRandom secureRandom = new SecureRandom();
        return start + secureRandom.nextInt(end + 1);
    }

    /**
     * 자릿수(length) 만큼 랜덤한 문자열을 대문자/소문자에 따라 반환 받습니다.
     *
     * @param length      자릿수
     * @param isUpperCase 대문자 여부
     * @return 랜덤한 문자열
     */
    public static String generateRandomStr(int length, boolean isUpperCase) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
        }
        return isUpperCase ? sb.toString().toUpperCase() : sb.toString().toLowerCase();
    }

    /**
     * 자릿수(length) 만큼 랜덤한 숫자 + 문자 조합을 대문자/소문자에 따라 반환 받습니다.
     *
     * @param length      자릿수
     * @param isUpperCase 대문자 여부
     * @return 랜덤한 숫자 + 문자 조합의 문자열
     */
    public static String generateRandomMixStr(int length, boolean isUpperCase) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return isUpperCase ? sb.toString() : sb.toString().toLowerCase();
    }
}
