package ir.kavoshgaran.keycloakauth.Controller.Authenticate;

import ir.kavoshgaran.keycloakauth.dto.rest.*;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final Authentication authentication;

    public AuthenticationController(Authentication authentication) {
        this.authentication = authentication;
    }

    @PostMapping(value = "/token", produces = MediaType.APPLICATION_STREAM_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<AccessTokenResponseDto>> getToken(@Valid GetTokenDto getTokenDto) {
        return ResponseEntity.ok(authentication.getToken(getTokenDto));
    }

    @PostMapping(value = "/refreshToken",produces = MediaType.APPLICATION_STREAM_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<AccessTokenResponseDto>> getTokenByRefreshToken(@Valid GetTokenByRefreshTokenDto refreshTokenDto) {
        return ResponseEntity.ok(authentication.getTokenByRefreshToken(refreshTokenDto));
    }

    @PostMapping(value = "/authenticateToken", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<Boolean>> authenticateToken(HttpServletRequest request) {
        return ResponseEntity.ok(authentication.authenticateToken(request.getHeader("Authorization")));
    }

    @PostMapping(value = "/authenticateOauth2Token", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<LinkedHashMap<String, Object>>> authenticate(HttpServletRequest request) {
        return ResponseEntity.ok(authentication.authenticateOauth2Token(request));
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_STREAM_JSON_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<KeycloakRegisterResponseDto>> register(@Valid RegisterDto registerDto) {
        return ResponseEntity.ok(authentication.register(registerDto));
    }


    @PostMapping(value = "/enableUser", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<Boolean>> enableUser(@Valid EnableUserDto enableUserDto) {
       return ResponseEntity.ok( authentication.enableUser(enableUserDto));
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<Boolean>> logout(@Valid UserLogoutDto userLogoutDto, HttpServletRequest request) {
        return ResponseEntity.ok(authentication.logout(userLogoutDto, request));
    }

    @PostMapping(value = "/changePassword", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<Boolean>> changePassword(@Valid ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        return ResponseEntity.ok(authentication.changePassword(changePasswordDto, request));
    }

    @PostMapping(value = "/resetPassword", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<Mono<Boolean>> resetPassword(@Valid ResetPasswordDto resetPasswordDto) {
        return ResponseEntity.ok(authentication.resetPassword(resetPasswordDto));
    }
}
