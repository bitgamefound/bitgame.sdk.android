package com.oro.mergesdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
/**
 * 验证用户输入的数据格式是否正确
 * @author Administrator
 *
 */
public class Oro_Verify
{
	/**
	 * 
	 * @param account
	 *            账号
	 * @return 格式是否正确
	 */
	//验证个性账号格式
	public static Boolean CheckAccount(String account)
	{
		Pattern pattern = Pattern.compile("[a-zA-Z][0-9a-zA-Z_]{5,19}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(account);
		return matcher.matches();
	}
	//验证邮箱格式
	public static Boolean CheckEmail(String email){
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]+@[0-9a-zA-Z]+\\.[0-9a-zA-Z]+$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		Log.i("zytx", matcher.matches()+"");
		return matcher.matches();
		
	}
/**
 *  * public static boolean isPhoneNumberValid(String phoneNumber) {
		boolean isValid = false;
		String expression = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|17[0,6,7,8]|14[5,7])\\d{8}$";
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(phoneNumber);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
 * */	
	
	//验证手机号格式
	public static Boolean CheckTell(String tell){
		Pattern pattern = Pattern.compile("[1]\\d{10}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(tell);
		return matcher.matches();
	}
	
	public static int GetAccountType(String allaccount){
		Log.i("zytx", "ACCOUNT" + allaccount);
		if(CheckAccount(allaccount)){
			return 1;
		}if (CheckEmail(allaccount)){
			return 3;
		}if(CheckTell(allaccount)){
			return 2;
		}
		return -1;
	}
	/**
	 * @param password
	 *            密码
	 * @return 格式是否正确
	 */
	public static Boolean CheckPassword(String password)
	{
		Pattern pattern = Pattern.compile("\\S{6,20}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	/**
	 * 
	 * @param captcha
	 *            验证码
	 * @return 格式是否正确
	 */
	public static Boolean CheckCaptcha(String captcha)
	{
		Pattern pattern = Pattern.compile("[0-9a-zA-Z]{4}", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(captcha);
		return matcher.matches();
	}
}
