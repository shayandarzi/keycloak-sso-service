package ir.kavoshgaran.keycloakauth.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAPIHelper {

    @Value("${keycloak.auth-server-url}")
    private String BASE_URL;
    @Value("${keycloak.realm}")
    private String REALM;

    public String getTokenUrl() {
        return BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/token";
    }

    public String getUserUrl() {
        return BASE_URL + "/admin/realms/" + REALM + "/users";
    }

    public String getUserInfoUrl() {
        return BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/userinfo";
    }

    public String getSearchUrl(String username) {
        return BASE_URL + "/admin/realms/" + REALM + "/users?search=" + username;
    }

    public String enableUserUrl(String userId) {
        return BASE_URL + "/admin/realms/" + REALM + "/users/" + userId;
    }

    public String userLogoutUrl() {
        return BASE_URL + "/realms/" + REALM + "/protocol/openid-connect/logout";
    }

    public String changePasswordUrl() {
        return BASE_URL + "/realms/" + REALM + "/account/credentials/password";
    }
    public String resetPasswordUrl(String userId){
        return BASE_URL + "/admin/realms/" + REALM + "/users/" + userId + "/reset-password";
    }

}
