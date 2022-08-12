package ir.kavoshgaran.keycloakauth.dto.rest;

import java.io.Serializable;

public class AccessTokenResponseDto implements Serializable {
    private String accessToken;
    private String refreshToken;
    private long expireIn;
    private long expireInRefresh;
    private String tokenType;

    public AccessTokenResponseDto() {
    }

    public AccessTokenResponseDto(String accessToken, String refreshToken, long expireIn, long expireInRefresh, String tokenType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expireIn = expireIn;
        this.expireInRefresh = expireInRefresh;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(long expireIn) {
        this.expireIn = expireIn;
    }

    public long getExpireInRefresh() {
        return expireInRefresh;
    }

    public void setExpireInRefresh(long expireInRefresh) {
        this.expireInRefresh = expireInRefresh;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
