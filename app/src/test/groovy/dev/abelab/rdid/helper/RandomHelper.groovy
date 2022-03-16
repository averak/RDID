package dev.abelab.rdid.helper


import org.apache.commons.lang3.RandomStringUtils
import org.jeasy.random.EasyRandom

/**
 * ランダム値を扱うヘルパー
 */
class RandomHelper {

    private static final EasyRandom easyRandom = new EasyRandom()

    /**
     * ランダムなメールアドレスを生成
     *
     * @return email
     */
    static String email() {
        return RandomStringUtils.randomAlphanumeric(10) + "@example.com"
    }

    /**
     * ランダムなアルファベット+数字の文字列を生成
     *
     * @param length
     * @return 文字列
     */
    static String alphanumeric(final Integer length) {
        return RandomStringUtils.randomAlphanumeric(length)
    }

    /**
     * ランダム値を格納したモックオブジェクトを返す
     *
     * @param clazz
     * @return モックオブジェクト
     */
    static <T> T mock(final Class<T> clazz) {
        return easyRandom.nextObject(clazz)
    }
}
