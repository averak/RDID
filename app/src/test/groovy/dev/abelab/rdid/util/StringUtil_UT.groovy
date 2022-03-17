package dev.abelab.rdid.util

import dev.abelab.rdid.BaseSpecification

/**
 * 文字列ユーティリティの単体テスト
 */
class StringUtil_UT extends BaseSpecification {

    def "checkLengthIsWithinRange: 文字列の長さが範囲内に収まるかチェック"() {
        when:
        def result = StringUtil.checkLengthIsWithinRange(string, min, max)

        then:
        result == expectedResult

        where:
        string   | min | max || expectedResult
        null     | 0   | 10  || false
        ""       | 0   | 10  || true
        ""       | 1   | 10  || false
        " "      | 0   | 10  || true
        " "      | 1   | 10  || false
        "a"      | 1   | 10  || true
        "a"      | 2   | 10  || false
        "a" * 10 | 1   | 10  || true
        "a" * 11 | 1   | 10  || false
    }

}
