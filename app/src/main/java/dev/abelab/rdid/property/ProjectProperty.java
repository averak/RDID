package dev.abelab.rdid.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * プロジェクトのプロパティ
 */
@Data
@Configuration
@ConfigurationProperties("project")
public class ProjectProperty {

    /**
     * プロジェクト名
     */
    String name;

    /**
     * バージョン
     */
    String version;

    /**
     * Swagger
     */
    Swagger swagger;

    /**
     * 管理者アカウント
     */
    AdminAccount adminAccount;

    @Data
    public static class Swagger {

        /**
         * プロトコル
         */
        String protocol;

        /**
         * ホスト名
         */
        String hostname;
    }

    @Data
    public static class AdminAccount {

        /**
         * ファーストネーム
         */
        String firstName;

        /**
         * ラストネーム
         */
        String lastName;

        /**
         * メールアドレス
         */
        String email;

        /**
         * パスワード
         */
        String password;

        /**
         * 入学年度
         */
        Integer admissionYear;

    }

}
