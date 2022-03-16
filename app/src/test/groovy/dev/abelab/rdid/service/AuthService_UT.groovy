package dev.abelab.rdid.service

import dev.abelab.rdid.BaseSpecification
import dev.abelab.rdid.enums.UserStatusEnum
import dev.abelab.rdid.exception.BaseException
import dev.abelab.rdid.exception.ErrorCode
import dev.abelab.rdid.exception.NotFoundException
import dev.abelab.rdid.exception.UnauthorizedException
import dev.abelab.rdid.helper.RandomHelper
import dev.abelab.rdid.model.UserModel
import dev.abelab.rdid.repository.UserRepository
import dev.abelab.rdid.util.AuthUtil
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Unroll

/**
 * AuthUtilの単体テスト
 */
class AuthService_UT extends BaseSpecification {

    @Autowired
    AuthService sut

    @SpringBean
    UserRepository userRepository = Mock();

    @SpringBean
    AuthUtil authUtil = Mock();

    def "getLoginUser: ログインユーザを取得"() {
        given:
        final bearerToken = RandomHelper.alphanumeric(10)
        final credentials = "Bearer " + bearerToken
        final user = UserModel.builder().status(UserStatusEnum.ACTIVE).build()

        when:
        def result = sut.getLoginUser(credentials)

        then:
        result == user
        1 * this.authUtil.verifyCredentials(bearerToken) >> 1
        1 * this.userRepository.selectById(1) >> Optional.of(user)
    }

    @Unroll
    def "getLoginUser: クレデンシャルがBearerタイプじゃない場合は401エラー"() {
        when:
        sut.getLoginUser(credentials)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN))

        where:
        credentials << [
            RandomHelper.alphanumeric(10),
            "Basic " + RandomHelper.alphanumeric(10)
        ]
    }

    def "getLoginUser: ユーザが存在しない場合は404エラー"() {
        given:
        final bearerToken = RandomHelper.alphanumeric(10)
        final credentials = "Bearer " + bearerToken

        when:
        sut.getLoginUser(credentials)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new NotFoundException(ErrorCode.NOT_FOUND_USER))
        1 * this.authUtil.verifyCredentials(bearerToken) >> 1
        1 * this.userRepository.selectById(1) >> Optional.empty()
    }

    def "getLoginUser: 退会済みユーザの場合は401エラー"() {
        given:
        final bearerToken = RandomHelper.alphanumeric(10)
        final credentials = "Bearer " + bearerToken
        final user = UserModel.builder().status(UserStatusEnum.NOT_ACTIVE).build()


        when:
        sut.getLoginUser(credentials)

        then:
        final BaseException exception = thrown()
        verifyException(exception, new UnauthorizedException(ErrorCode.NOT_ACTIVE_USER_CANNOT_LOGIN))
        1 * this.authUtil.verifyCredentials(bearerToken) >> 1
        1 * this.userRepository.selectById(1) >> Optional.of(user)
    }

}
