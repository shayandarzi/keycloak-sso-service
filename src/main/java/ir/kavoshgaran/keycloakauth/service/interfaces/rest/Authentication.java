package ir.kavoshgaran.keycloakauth.service.interfaces.rest;

import ir.kavoshgaran.keycloakauth.dto.rest.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedHashMap;

public interface Authentication {

    /**
     * This method is used for generate token by sso server
     *
     * @param getTokenDto {@link GetTokenDto}
     * @return AccessTokenResponseDto {@link AccessTokenResponseDto}
     */

    Mono<AccessTokenResponseDto> getToken(@Valid GetTokenDto getTokenDto);

    /**
     * This method is used for authenticate token
     *
     * @param authorization {@link String}
     * @return {@code true} if authenticate successfully and {@code false} otherwise
     */

    Mono<Boolean> authenticateToken(String authorization);

    /**
     * This method is used for authenticate token
     *
     * @param request {@link HttpServletRequest}
     * @return LinkedHashMap<String, Object>
     */
    Mono<LinkedHashMap<String, Object>> authenticateOauth2Token(HttpServletRequest request);

    /**
     * This method is used for generate token via refresh token

     * @param refreshTokenDto {@link GetTokenByRefreshTokenDto}
     * @return AccessTokenResponseDto {@link AccessTokenResponseDto}
     */

    Mono<AccessTokenResponseDto> getTokenByRefreshToken(@Valid GetTokenByRefreshTokenDto refreshTokenDto);

    /**
     * This method is used for register a new user in sso server

     * @param registerDto {@link RegisterDto}
     * @return {@code true} if registration successfully and {@code false} otherwise
     * */
    Mono<KeycloakRegisterResponseDto> register(@Valid RegisterDto registerDto);

    Mono<Boolean>  enableUser(EnableUserDto enableUserDto);

    Mono<Boolean> logout(UserLogoutDto userLogoutDto,HttpServletRequest request);

    Mono<Boolean> changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request);

    Mono<Boolean> resetPassword(ResetPasswordDto resetPasswordDto);

}
