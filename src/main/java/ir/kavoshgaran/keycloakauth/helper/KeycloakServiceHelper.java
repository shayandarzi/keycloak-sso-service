package ir.kavoshgaran.keycloakauth.helper;

import ir.kavoshgaran.keycloakauth.dto.rest.AccessTokenResponseDto;
import org.keycloak.representations.AccessTokenResponse;
import reactor.core.publisher.Mono;

public class KeycloakServiceHelper {

    public static Mono<AccessTokenResponseDto> getAccessTokenResponseDto(AccessTokenResponse accessTokenResponse) {
        AccessTokenResponseDto responseDto = new AccessTokenResponseDto();
        responseDto.setAccessToken(accessTokenResponse.getToken());
        responseDto.setRefreshToken(accessTokenResponse.getRefreshToken());
        responseDto.setExpireIn(accessTokenResponse.getExpiresIn());
        responseDto.setExpireInRefresh(accessTokenResponse.getRefreshExpiresIn());
        responseDto.setTokenType(accessTokenResponse.getTokenType());
        return Mono.just(responseDto);
    }
}
