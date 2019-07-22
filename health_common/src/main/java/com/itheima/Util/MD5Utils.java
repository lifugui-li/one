package com.itheima.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * 使用md5的算法进行加密
	 */
	public static String md5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(
					plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	public static void main(String[] args) {
		//System.out.println(md5("1234"));
		// 加密
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		//$2a$10$ygSUziz01hVLJ3ivhtFvHOy4hJQad7fL9GVJMNFR1KaMucQmnGL/a
		//$2a$10$CbySwMekdLVopyGK73VhVOauLDaBZBK2A1PIgSser4UolDITvkn4a
		//$2a$10$./FSTtYmwEe02UTclJIZW.5RkVDv5od4/TM7cN0lOkRzOgrq6jLYi
		//System.out.println(encoder.encode("1234"));
		// 验证密码
		// 第一个参数：明文密码 从前端传过来的密码
		// 第二个参数：加密的密码，从数据库来的密码
		System.out.println(encoder.matches("1234", "$2a$10$ygSUziz01hVLJ3ivhtFvHOy4hJQad7fL9GVJMNFR1KaMucQmnGL/a"));
		System.out.println(encoder.matches("1234", "$2a$10$CbySwMekdLVopyGK73VhVOauLDaBZBK2A1PIgSser4UolDITvkn4a"));
		System.out.println(encoder.matches("1234", "$2a$10$./FSTtYmwEe02UTclJIZW.5RkVDv5od4/TM7cN0lOkRzOgrq6jLYi"));
	}

}