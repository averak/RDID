package dev.abelab.rdid.repository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.rdid.db.entity.Token;
import dev.abelab.rdid.db.entity.TokenExample;
import dev.abelab.rdid.db.mapper.TokenMapper;
import dev.abelab.rdid.exception.ErrorCode;
import dev.abelab.rdid.exception.UnauthorizedException;

@RequiredArgsConstructor
@Repository
public class TokenRepository {

    private final TokenMapper tokenMapper;

    /**
     * トークンを作成
     *
     * @param token トークン
     *
     * @return トークンID
     */
    public int insert(final Token token) {
        return this.tokenMapper.insertSelective(token);
    }

    /**
     * トークン文字列からトークンを取得
     *
     * @param token トークン文字列
     *
     * @return トークン
     */
    public Token selectByToken(final String token) {
        final var example = new TokenExample();
        example.createCriteria().andTokenEqualTo(token);

        final var tokens = tokenMapper.selectByExample(example);
        if (tokens.isEmpty()) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        } else {
            return tokens.get(0);
        }
    }

}
