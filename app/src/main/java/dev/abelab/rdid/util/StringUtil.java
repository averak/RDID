package dev.abelab.rdid.util;

import java.util.Objects;

import org.codehaus.plexus.util.StringUtils;

/**
 * 文字列のユーティリティ
 */
public class StringUtil {

    /**
     * 文字列の長さが範囲内に収まっているかチェック
     * 
     * @param string 文字列
     * @param min 最小文字数
     * @param max 最大文字数
     * @return 文字列の長さが範囲内に収まるか
     */
    public static Boolean checkLengthIsWithinRange(final String string, final Integer min, final Integer max) {
        if (Objects.isNull(string)) {
            return false;
        }

        if (StringUtils.isBlank(string) && min != 0) {
            return false;
        } else {
            return string.length() >= min && string.length() <= max;
        }
    }

}
