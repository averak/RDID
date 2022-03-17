package dev.abelab.rdid

import dev.abelab.rdid.exception.BaseException
import groovy.sql.Sql
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * テストの基底クラス
 */
@EnableAutoConfiguration
@SpringBootTest
abstract class BaseSpecification extends Specification {

    /**
     * SQL Handler
     */
    final Sql sql = Sql.newInstance(
        "jdbc:mysql://localhost:3307/rdid?useSSL=false&allowPublicKeyRetrieval=true&enabledTLSProtocols=TLSv1.2",
        "rdid",
        "rdid",
        "com.mysql.cj.jdbc.Driver"
    )

    /**
     * setup before test case
     */
    def setup() {
        // DBを初期化
        sql.execute "SET foreign_key_checks = 0"
        sql.execute "TRUNCATE user"
        sql.execute "TRUNCATE user_group"
        sql.execute "TRUNCATE user_group_role"
        sql.execute "TRUNCATE user_group_member"
        sql.execute "SET foreign_key_checks = 1"
    }

    /**
     * 例外を検証
     *
     * @param thrownException 発生した例外
     * @param expectedException 期待する例外
     */
    void verifyException(final BaseException thrownException, final BaseException expectedException) {
        assert thrownException.getClass() == expectedException.getClass()
        assert thrownException.getHttpStatus() == expectedException.getHttpStatus()
        assert thrownException.getErrorCode() == expectedException.getErrorCode()
    }

}
