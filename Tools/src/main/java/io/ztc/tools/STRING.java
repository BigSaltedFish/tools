package io.ztc.tools;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串检测
 */
public class STRING {
    /**
     * 邮箱检测
     * @param email 可能是Email的字符串
     * @return 是否是Email
     */
    public static boolean email(String email) {
        Pattern pattern = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 非空验证
     * @param data 源字符串
     * @return 是否为空
     */
    public static boolean empty(String data) {
        return TextUtils.isEmpty(data);
    }

    /**
     * 邮箱验证
     *
     * @param data 可能是Email的字符串
     * @return 是否是Email
     */
    public static boolean isEmail(String data) {
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return data.matches(expr);
    }

    /**
     * 移动手机号码验证
     * @param data 可能是手机号码字符串
     * @return 是否是手机号码
     */
    public static boolean phone(String data) {
        //String expr = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        String expr = "^1[3|4|5|7|8]\\d{9}$";
        return data.matches(expr);
    }

    /**
     * 微信号码验证
     * @param wxCode 检测字段
     * @return 是否是微信
     */

    public static boolean wxCode(String wxCode){

        boolean flag = false;
        if(!TextUtils.isEmpty(wxCode)){

            if(!TextUtils.isEmpty(wxCode)){
                if(wxCode.contains("@")){  //验证邮箱号
                    String check = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?.)+[a-zA-Z]{2,}$";
                    Pattern regex = Pattern.compile(check);
                    Matcher matcher = regex.matcher(wxCode);
                    flag = matcher.matches();
                }else {
                    String reg1 = "[1-9]\\d{5,19}";  //qq号 6 - 20
                    String reg2 = "1[3-9]\\d{9}";  //qq号或者手机号 11
                    String reg3 = "[a-zA-Z][-_a-zA-Z0-9]{5,19}"; //微信号带字母的 6-20
                    flag = wxCode.matches(reg1) || wxCode.matches(reg2) || wxCode.matches(reg3);
                }
            }

        }
        return flag;

    }


    /**
     * 判断是否含有特殊字符
     * @param str 检测字段
     * @return true为包含，false为不包含
     */
    public static boolean specialIn(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 只含字母和数字
     * @param data 只包含字母和数字的字符串
     * @return 是否只包含字母和数字
     */
    public static boolean onlyNumLetIn(String data) {
        String expr = "^[A-Za-z0-9]+$";
        return data.matches(expr);
    }

    /**
     * 只含数字
     * @param data 只包含数字的字符串
     * @return 是否只包含数字
     */
    public static boolean onlyNumIn(String data) {
        String expr = "^[0-9]+$";
        return data.matches(expr);
    }

    /**
     * 只含字母
     * @param data 只包含字母的字符串
     * @return 是否只包含字母
     */
    public static boolean onlyLetIn(String data) {
        String expr = "^[A-Za-z]+$";
        return data.matches(expr);
    }

    /**
     * 只是中文
     * @param data 可能是中文的字符串
     * @return 是否只是中文
     */
    public static boolean chinese(String data) {
        String expr = "^[\u0391-\uFFE5]+$";
        return data.matches(expr);
    }

    /**
     * 包含中文
     * @param data 可能包含中文的字符串
     * @return 是否包含中文
     */
    public static boolean chineseIn(String data) {
        String chinese = "[\u0391-\uFFE5]";
        if (empty(data)) {
            for (int i = 0; i < data.length(); i++) {
                String temp = data.substring(i, i + 1);
                boolean flag = temp.matches(chinese);
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 小数点位数
     * @param data   可能包含小数点的字符串
     * @param length 小数点后的长度
     * @return 是否小数点后有length位数字
     */
    public static boolean decimalPointIn(String data, int length) {
        String expr = "^[1-9][0-9]+\\.[0-9]{" + length + "}$";
        return data.matches(expr);
    }

    /**
     * 身份证号码验证
     * @param data 可能是身份证号码的字符串
     * @return 是否是身份证号码
     */
    public static boolean idCard(String data) {
        String expr = "^[0-9]{17}[0-9xX]$";
        return data.matches(expr);
    }

    /**
     * 邮政编码验证
     * @param data 可能包含邮政编码的字符串
     * @return 是否是邮政编码
     */
    public static boolean postCode(String data) {
        String expr = "^[0-9]{6,10}";
        return data.matches(expr);
    }

    /**
     * 长度验证
     * @param data   源字符串
     * @param length 期望长度
     * @return 是否是期望长度
     */
    public static boolean length(String data, int length) {

        return data != null && data.length() == length;
    }

    /**
     * 长度区间验证
     * @param data 源字符串
     * @param start 初始长度
     * @param end 结束长度
     * @return 是否
     */
    public static boolean length(String data, int start,int end) {
        return data.length() >= start && data.length() <= end;
    }

    /**
     * 字符串是否是空，true=非空，false=空
     * @param str 源字符串
     * @return 是否
     */
    public static boolean notNull(String str) {
        if (str == null) {
            return false;
        }
        if (str.equals("")) {
            return false;
        }
        return !str.equals("null");
    }

    /**
     * 逗号分割转换为LIST
     * @param text
     * @return
     */
    public static List<String> list(String text){
        if (text!=null){
            return Arrays.asList(text.split(","));
        }
        return null;
    }

    /**
     * 自定义符号分割转ArrayList
     * @param text
     * @param split
     * @return
     */
    public static ArrayList<String> list(String text, String split){
        if (text!=null){
            try {
                if (text.matches(".*"+split+".*")){

                    List<String> name = Arrays.asList(text.split(split));
                    return new ArrayList<String>(name);
                }
                ArrayList<String> list = new ArrayList<>();
                list.add(text);
                return list;
            }catch (Exception e){
                e.printStackTrace();
                ArrayList<String> list = new ArrayList<>();
                list.add(text);
                return list;
            }
        }
        return new ArrayList<>();
    }

}
