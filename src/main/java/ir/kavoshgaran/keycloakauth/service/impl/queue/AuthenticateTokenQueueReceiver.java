package ir.kavoshgaran.keycloakauth.service.impl.queue;

import com.rabbitmq.client.Channel;
import ir.kavoshgaran.keycloakauth.dto.queue.AuthenticateTokenRequest;
import ir.kavoshgaran.keycloakauth.dto.queue.AuthenticateTokenResponse;
import ir.kavoshgaran.keycloakauth.exception.BitrahBaseException;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.Authentication;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.IQueueLogService;
import ir.kavoshgaran.keycloakauth.service.interfaces.queue.IAuthenticateTokenQueueReceiver;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.ws.rs.NotAcceptableException;
import java.io.IOException;

@Component
public class AuthenticateTokenQueueReceiver implements IAuthenticateTokenQueueReceiver {

    private Authentication authentication;

    private IQueueLogService queueLogService;

    public AuthenticateTokenQueueReceiver(Authentication authentication, IQueueLogService queueLogService) {
        this.authentication = authentication;
        this.queueLogService = queueLogService;
    }

    /**
     * this method is waiting for get 'authentication_token' request message from rabbitmq and check that token is valid or not
     * it replies the response through the queue that set in message header
     *
     * @param request
     * @param message
     * @return
     */
    @Override
    @RabbitListener(queues = {"${spring.queue.authenticate.token}"})
    public AuthenticateTokenResponse receive(@Payload AuthenticateTokenRequest request, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        if (message.getMessageProperties().getReplyTo() != null) {
            queueLogService.saveRequest(request.toString(), message);
            AuthenticateTokenResponse response = new AuthenticateTokenResponse();
            try {
                if (request.getToken() == null)
                    throw new NotAcceptableException("token must not be null");
                String token = checkTokenFormat(request.getToken());
                Mono<Boolean> keycloakResponse = authentication.authenticateToken(token);
                Boolean isAuthorized = keycloakResponse.block();
                if (isAuthorized == null)
                    throw new NotAcceptableException("error on authentication service");
                response.setAuthenticate(isAuthorized);
            } catch (BitrahBaseException e) {
                response.setErrorMessage(e.getMessage());
            } catch (Exception e) {
                channel.basicNack(deliveryTag, false, true);
            }
            queueLogService.saveResponse(response.toString(), message);
            return response;
        } else {
            message.getMessageProperties().setReplyTo("x-death");
        }
        return null;
    }

    private String checkTokenFormat(String token) {
        String tokenType = "Bearer ";
        if (token.contains(tokenType))
            return token;
        return tokenType.concat(token);
    }
}
