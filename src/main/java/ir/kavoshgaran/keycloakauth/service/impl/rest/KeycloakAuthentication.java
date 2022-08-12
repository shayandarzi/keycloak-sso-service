package ir.kavoshgaran.keycloakauth.service.impl.rest;

import ir.kavoshgaran.keycloakauth.dto.rest.*;
import ir.kavoshgaran.keycloakauth.exception.BitrahAuthenticationException;
import ir.kavoshgaran.keycloakauth.helper.KeycloakAPIHelper;
import ir.kavoshgaran.keycloakauth.helper.KeycloakServiceHelper;
import ir.kavoshgaran.keycloakauth.helper.RestParameterHelper;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.Authentication;
import org.keycloak.representations.AccessTokenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Validated
public class KeycloakAuthentication implements Authentication {

    @Value("${keycloak.realm}")
    private String REALM;
    @Value("${keycloak.resource}")
    private String clientId;
    @Value("${client.secret.id}")
    private String clientSecret;

    private final WebClient webClient;
    private final KeycloakAPIHelper keycloakAPIHelper;
    private final String authorization = RestParameterHelper.AUTHORIZATION;
    private final String bearer = "Bearer ";
    private final String clientIdParameter= RestParameterHelper.CLIENT_ID;
    private final String clientSecretParameter = RestParameterHelper.CLIENT_SECRET;

    public KeycloakAuthentication(WebClient webClient, KeycloakAPIHelper keycloakAPIHelper) {
        this.webClient = webClient;
        this.keycloakAPIHelper = keycloakAPIHelper;
    }

    @Override
    public Mono<AccessTokenResponseDto> getToken(@Valid GetTokenDto getTokenDto) {
        AccessTokenResponse accessTokenResponse = webClient.post().uri(keycloakAPIHelper.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(clientIdParameter, clientId).with("grant_type", "password")
                        .with(clientSecretParameter, clientSecret)
                        .with("username", getTokenDto.getUsername())
                        .with("password", getTokenDto.getPassword()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BitrahAuthenticationException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED)))
                .bodyToMono(AccessTokenResponse.class)
                .block();
        return KeycloakServiceHelper.getAccessTokenResponseDto(accessTokenResponse);
    }

    @Override
    public Mono<AccessTokenResponseDto> getTokenByRefreshToken(GetTokenByRefreshTokenDto refreshTokenDto) {
        AccessTokenResponse accessTokenResponse = webClient.post().uri(keycloakAPIHelper.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(clientIdParameter, clientId)
                        .with(clientSecretParameter, clientSecret)
                        .with("grant_type", "refresh_token")
                        .with("refresh_token", refreshTokenDto.getRefreshToken()))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BitrahAuthenticationException("bad request", HttpStatus.BAD_REQUEST)))
                .bodyToMono(AccessTokenResponse.class)
                .block();
        return KeycloakServiceHelper.getAccessTokenResponseDto(accessTokenResponse);

    }

    public Mono<Boolean> authenticateToken(String Authorization) {
        HttpStatus httpStatus = webClient.get().uri(keycloakAPIHelper.getUserInfoUrl())
                .header(authorization, Authorization)
                .exchange()
                .map(ClientResponse::statusCode)
                .block();

        return Mono.just(httpStatus == HttpStatus.OK);
    }

    @Override
    public Mono<LinkedHashMap<String, Object>> authenticateOauth2Token(HttpServletRequest request) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        String[] authorizations = clientCredentials(request.getHeader(authorization));
        if (authorizations[0].equals(clientId) && authorizations[1].equals(clientSecret)) {
            String token = request.getParameter("token");
            HttpStatus httpStatus = webClient.get().uri(keycloakAPIHelper.getUserInfoUrl())
                    .header(authorization, bearer + token)
                    .exchange()
                    .map(ClientResponse::statusCode)
                    .block();
            if (httpStatus == HttpStatus.OK) {
                response.put("active", true);
            } else {
                response.put("active", false);
                response.put("error", "invalid_token");
            }
            return Mono.just(response);
        } else {
            response.put("error", "Client ID or client secret is invalid");
        }
        return Mono.just(response);
    }

    @Override
    public Mono<KeycloakRegisterResponseDto> register(@Valid RegisterDto registerDto) {
        Credentials credentials = new Credentials("password", registerDto.getPassword(), false);
        List<Credentials> credentialsList = new ArrayList<>();
        credentialsList.add(credentials);
        SearchDto searchDto;
        KeycloakRegisterDto keycloakRegisterDto = new KeycloakRegisterDto(registerDto.getUsername(), false, credentialsList);
        HttpStatus httpStatus = webClient.post().uri(keycloakAPIHelper.getUserUrl())
                .header(authorization, bearer + getClientToken().getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(keycloakRegisterDto))
                .exchange()
                .map(ClientResponse::statusCode)
                .block();
        if (httpStatus == HttpStatus.CREATED) {
            searchDto = webClient.get().uri(keycloakAPIHelper.getSearchUrl(registerDto.getUsername()))
                    .header(authorization, bearer + getClientToken().getToken())
                    .retrieve()
                    .bodyToMono(SearchDto.class).block();

            Object o = searchDto.get(0);
            if (o instanceof LinkedHashMap) {
                LinkedHashMap<String, String> le = (LinkedHashMap<String, String>) o;
                return Mono.just(new KeycloakRegisterResponseDto(le.get("id")));
            }
        }
        if (httpStatus == HttpStatus.CONFLICT) {
            throw new BitrahAuthenticationException("duplicate username", HttpStatus.CONFLICT);
        }

        return null;
    }

    @Override
    public Mono<Boolean> enableUser(EnableUserDto enableUserDto) {
        KeycloakEnableDto keycloakEnableDto = new KeycloakEnableDto();
        keycloakEnableDto.setEnabled(true);
        HttpStatus httpStatus = webClient.put().uri(keycloakAPIHelper.enableUserUrl(enableUserDto.getUserId()))
                .header(authorization, bearer + getClientToken().getToken())
                .body(BodyInserters.fromObject(keycloakEnableDto))
                .exchange()
                .map(ClientResponse::statusCode)
                .block();
        return Mono.just(httpStatus == HttpStatus.OK);
    }

    @Override
    public Mono<Boolean> logout(UserLogoutDto userLogoutDto, HttpServletRequest request) {
        HttpStatus httpStatus = webClient.post().uri(keycloakAPIHelper.userLogoutUrl())
                .header(authorization, request.getHeader(authorization))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("refresh_token", userLogoutDto.getRefreshToken())
                        .with(clientIdParameter, clientId)
                        .with(clientSecretParameter, clientSecret))
                .exchange()
                .map(ClientResponse::statusCode)
                .block();
        return Mono.just(httpStatus == HttpStatus.NO_CONTENT);

    }

    @Override
    public Mono<Boolean> changePassword(ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        HttpStatus httpStatus = webClient.post().uri(keycloakAPIHelper.changePasswordUrl())
                .header(authorization, request.getHeader(authorization))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(changePasswordDto))
                .exchange()
                .map(ClientResponse::statusCode)
                .block();
        if (httpStatus == HttpStatus.NO_CONTENT) {
            return Mono.just(true);
        } else if (httpStatus == HttpStatus.BAD_REQUEST) {
            throw new BitrahAuthenticationException("current password is incorrect", HttpStatus.BAD_REQUEST);
        }
        return Mono.just(false);
    }

    @Override
    public Mono<Boolean> resetPassword(ResetPasswordDto resetPasswordDto) {
        KeycloakResetPasswordDto keycloakResetPasswordDto = new KeycloakResetPasswordDto();
        keycloakResetPasswordDto.setValue(resetPasswordDto.getNewPassword());
        HttpStatus httpStatus = webClient.put().uri(keycloakAPIHelper.resetPasswordUrl(resetPasswordDto.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .header(authorization, bearer + getClientToken().getToken())
                .body(BodyInserters.fromObject(keycloakResetPasswordDto))
                .exchange()
                .map(ClientResponse::statusCode)
                .block();

        return Mono.just(httpStatus == HttpStatus.NO_CONTENT);
    }

    private AccessTokenResponse getClientToken() {
        return webClient.post().uri(keycloakAPIHelper.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(clientIdParameter, clientId)
                        .with(clientSecretParameter, clientSecret)
                        .with("grant_type", "client_credentials"))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();
    }

    private String[] clientCredentials(String authorization) {
        String[] values = null;
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            values = credentials.split(":", 2);
        }
        return values;
    }
}
