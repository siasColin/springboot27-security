package cn.net.ssd.common.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证工具
 * lsz
 */
public class RegexUtils {


    //------------------常量定义 目前项目中遇到的,后续可更新
   /**
     * 手机号码正则表达式
     */
    public static final String MOBILE ="^(1[1-9][0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    /**
     * 电话号正则校验
     */
    public static final String MOB ="^(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,15}$";
    /**
     * Integer正则表达式 ^-?(([1-9]\d*$)|0)
     */
    public static final String  INTEGER = "^-?(([1-9]\\d*$)|0)";
    /**
     * 正整数正则表达式 >=0 ^[1-9]\d*|0$
     */
    public static final String  INTEGER_NEGATIVE = "^[1-9]\\d*|0$";
    /**
     * 负整数正则表达式 <=0 ^-[1-9]\d*|0$
     */
    public static final String  INTEGER_POSITIVE = "^-[1-9]\\d*|0$";

    /**
     * 坐标正则校验,整数位最多4位,小数位最多6位的数字
     */
    public static final String COORDINATE = "^(\\-|\\+)?(\\d){1,4}(\\.(\\d){1,10})?$";
    /**
     * 浮点校验,非负整数最多8位，小数最多2位
     * ^(\+)?(\d){1,8}(\.(\d){0,2})?$
     * ^[0-9]{1,11}+(.[0-9]{1,6})?$
     * ^(([1-9]{1}\\d{1,8}))(\\.(\\d){0,2})?$
     */
    public static final String DASDECIMAL ="^(\\+)?(\\d){1,8}(\\.(\\d){1,2})?$";

    public static final String DASYEAR ="^(\\+)?(\\d){1,4}(\\.(\\d){0})?$";
    /**
     * 整数校验,非负整数长度10位
     * ^(\+)?(\d)+((\d){1,10})?$
     */
    public static final String NUMBER = "^[1-9]\\d{1,10}|0$";

    /**
     * 判断是否为空,做必填项校验
     * @param str
     * @return
     */
    public static boolean isNull(String str){
        return null == str || str.trim().length() <= 0 ? true : false ;
    }


    /**
     * 唯一值 校验。
     * @param tableName
     * @param id
     * @param code
     * @param val
     * @return
     */
    /*public  boolean onlyKey(String tableName,String id,String code,String val){
        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("val",val);
        map.put("code",code);
        map.put("tableName",tableName);
        return iCommonService.fromVerifyByCode(map);
    }*/
    /**
     * 判断字符串中是否包含中文
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 字符串长度校验,为空则不校验
     * @param str
     * @param len
     * @return
     */
    public static boolean isLength(String str,Integer len){
        if (null == str || str.length()<=0) {
            return true;
        }
        return str.length() <= len ? true : false ;
    }
    /**
     * 判断坐标格式是否正确
     * @return
     */
    public static boolean isCoordinate(BigDecimal decimal){
        //不存在为空则直接返回错误
        if(null != decimal && decimal.toString().length()>0){
            String str =String.valueOf(decimal);
            return Regular(str,COORDINATE);
        }
        return false;
    }
    public static boolean isCoordinate(BigDecimal decimal,boolean flag){
        if(!flag){
          if(null != decimal && decimal.toString().length()>0){
              String str =String.valueOf(decimal);
              return Regular(str,COORDINATE);
          }
          return true;
        }
        //不存在为空则直接返回错误
        if(null != decimal && decimal.toString().length()>0){
            String str =String.valueOf(decimal);
            return Regular(str,COORDINATE);
        }
        return false;
    }

    /**
     * 判断是否为非负10位整数
     * @param lon
     * @return
     */
    public static boolean isNumber(Long lon){

        if(null != lon && lon.toString().length()>0){
            String str =String.valueOf(lon);
             if(Regular(str,NUMBER)){
                 if(lon > 2147483647){
                    return false;
                 }
                 return true;
             }
        }
        return true;

    }

    /**
     * 年份检查,暂时检查4位值,后期判断日期有效性
     * @param decimal
     * @return
     */

    public static boolean isYear(BigDecimal decimal){
        if(null != decimal && decimal.toString().length()>0){
            String str =String.valueOf(decimal);
            return Regular(str,DASYEAR);
        }
        return true;
    }
    /**
     * 判断是否为浮点值
     * @param decimal
     * @return
     */
    public static boolean isDecimal(BigDecimal decimal){
        if(null != decimal && decimal.toString().length()>0){
            String str =String.valueOf(decimal);
            return Regular(str,DASDECIMAL);
        }
        return true;

    }
    public static boolean isDecimal(String str){
        if(null != str && str.length()>0){
            return Regular(str,DASDECIMAL);
        }
        return true;

    }

    public static boolean isPhone(String mobile){
        return Regular(mobile,MOBILE);
    }

    public static boolean isMob(String mobile){
        return Regular(mobile,MOB);
    }



    /**
     * 处理类似 市,县,乡镇 上传拼接key_value值处理
     * @param str
     * @return
     */
    public static String isKeyValue(String str){
        if(null != str && str.trim().length()>0){
            String[] arrys = str.split("_");
            return arrys[1] != null ? arrys[1] : "";
        }
        return "";
    }
    /**
     * 匹配是否符合正则表达式pattern 匹配返回true
     * @param str Object对象,当数据不为null才校验,无值则不校验。
     * @param pattern 匹配模式
     * @return boolean
     */
    private static  boolean Regular(String str,String pattern){
        if( str != null && str.toString().length()>0){
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(str);
            return m.matches();
        }
        return true;

        /*if(null == str || str.trim().length()<=0)
            return false;
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();*/
    }


    public static void main(String args[]){


        System.out.println(isPhone("18500136413"));
        System.out.println(isPhone("111"));

    }

    public static List<String> getMatchList(final String str, final String reg, final boolean isCaseInsensitive) {
        ArrayList<String> result = new ArrayList<String>();
        Pattern pattern = null;
        if (isCaseInsensitive) {
            //编译正则表达式,忽略大小写
            pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        } else {
            //编译正则表达式,大小写敏感
            pattern = Pattern.compile(reg);
        }
        Matcher matcher = pattern.matcher(str);// 指定要匹配的字符串
        while (matcher.find()) { //此处find（）每次被调用后，会偏移到下一个匹配
            result.add(matcher.group());//获取当前匹配的值
        }
        result.trimToSize();
        return result;
    }
}
