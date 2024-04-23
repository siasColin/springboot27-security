package cn.net.ssd.common.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2024/4/8 8:46
 */
public class StringUtil {
    public static boolean checkNull(String value) {
        return value == null || (value != null && ("".equals(value.trim()) || "null".equalsIgnoreCase(value.trim())));
    }
    public static boolean isPureNumber(String input) {
        int len = input.length();
        int dotCount = 0; // 记录小数点数量
        boolean hasNegative = false; // 标记是否出现过负号

        if (len == 0) {
            return false; // 空字符串不是纯数字
        }

        // 检查负号（只允许出现在首位）
        if (input.charAt(0) == '-') {
            if (len == 1) {
                return false; // 只有负号，不是纯数字
            }
            hasNegative = true;
        }

        for (int i = (hasNegative ? 1 : 0); i < len; i++) {
            char c = input.charAt(i);

            // ASCII值在48到57之间（含）的字符对应于十进制数字0到9
            if (c >= 48 && c <= 57) {
                continue; // 数字字符，继续检查下一个字符
            }

            // 检查小数点
            if (c == '.') {
                if (dotCount > 0 || i == len - 1) {
                    // 多个小数点或小数点位于末尾，不是纯数字
                    return false;
                }
                dotCount++;
                continue;
            }

            // 非数字字符，返回false
            return false;
        }

        // 全部为数字、负号或一个小数点，返回true
        return true;
    }

    public static String getUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 从中文文本中提取第一个句子。
     * 
     * @param chineseText 待处理的中文文本字符串。
     * @return 返回提取出的第一个句子，如果没有找到句子，则返回空字符串。
     */
    public static String extractFirstSentence(String chineseText) {
        // 定义正则表达式，用以匹配第一个句子。句子以一个或多个非标点符号字符结尾，后面跟着一个标点符号。
        String regex = "([^：:。！；;]+[：:。！；;])";
        Pattern pattern = Pattern.compile(regex); // 编译正则表达式为模式。
        Matcher matcher = pattern.matcher(chineseText); // 创建匹配器，用于在文本中搜索模式。
        
        if (matcher.find()) { // 如果找到与模式匹配的文本。
            return matcher.group(); // 返回匹配到的文本。
        }
        
        return chineseText; // 如果未找到匹配的文本，则返回空字符串。
    }

    static String[][] alarmlevel= {
            //0    1          2          3        4     5
            {"1","红色","Ⅰ级/特别严重", "RED",    "Ⅰ",  "5"},
            {"2","橙色", "Ⅱ级/严重",   "ORANGE", "Ⅱ",  "6"},
            {"3","黄色", "Ⅲ级/较重",  "YELLOW", "Ⅲ", "7"},
            {"4","蓝色",  "Ⅳ级/一般",   "BLUE",  "Ⅳ",  "8"},
            {"10","提示",  "提示",  "UNKNOWN",  "Ⅴ",  "14"}
    };

    /**
     * 根据提供的字符串在预定义的alarmlevel二维数组中查找，返回对应位置的字符串。
     * @param str 要查找的字符串。
     * @param k 要返回的列索引。
     * @return 如果找到，则返回对应列索引的字符串；否则返回空字符串。
     */
    public static String getAlarmlevel(String str, int k) {
        // 遍历alarmlevel数组以查找匹配的字符串
        for(int i=0; i<5; i++) {
            for(int j=0; j<6; j++) {
                // 如果找到匹配的字符串，则返回对应列的字符串
                if(alarmlevel[i][j].equals(str))
                    return alarmlevel[i][k];
            }
        }
        // 如果未找到匹配的字符串，则返回空字符串
        return "";
    }
}
