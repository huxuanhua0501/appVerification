package net.app.util;

/**
 * @author hu_xuanhua_hua
 * @ClassName: StringUtils
 * @Description: TODO
 * @date 2018-12-10 15:53
 * @versoin 1.0
 **/
public class StringUtils {
    public static boolean isNotBlank(String currentPage) {
        return null != currentPage && !"".equals(currentPage.trim()) && !"null".equals(currentPage) && !"undefined".equals(currentPage);
    }

    public static boolean isBlank(String value) {
        return null == value || "".equals(value.trim()) || "null".equals(value);
    }

    public static String toCamel(String value) {
        StringBuilder sbResult = new StringBuilder();
        String[] terms = value.split("_");

        for(int i = 0; i < terms.length; ++i) {
            if (i == 0) {
                sbResult.append(terms[i]);
            }

            if (i != 0) {
                sbResult.append((new String(new char[]{terms[i].charAt(0)})).toUpperCase());
                sbResult.append(terms[i].substring(1));
            }
        }

        return sbResult.toString();
    }

    public static String toSqlFormt(String value) {
        StringBuilder sbResult = new StringBuilder();
        String[] valeList = value.split(",");

        for(int i = 0; i < valeList.length; ++i) {
            if (i == 0) {
                sbResult.append("'");
                sbResult.append(valeList[i]);
                sbResult.append("'");
            } else {
                sbResult.append(",");
                sbResult.append("'");
                sbResult.append(valeList[i]);
                sbResult.append("'");
            }
        }

        return sbResult.toString();
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else if (str1 != null) {
            return str1.equals(str2);
        } else {
            return str2 != null ? str2.equals(str1) : false;
        }
    }
}
