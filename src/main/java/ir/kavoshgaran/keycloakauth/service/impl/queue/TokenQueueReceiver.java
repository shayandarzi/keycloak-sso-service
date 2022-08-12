package ir.kavoshgaran.keycloakauth.service.impl.queue;

import com.rabbitmq.client.Channel;
import ir.kavoshgaran.keycloakauth.dto.queue.GetTokenRequest;
import ir.kavoshgaran.keycloakauth.dto.queue.GetTokenResponse;
import ir.kavoshgaran.keycloakauth.dto.rest.AccessTokenResponseDto;
import ir.kavoshgaran.keycloakauth.dto.rest.GetTokenDto;
import ir.kavoshgaran.keycloakauth.exception.BitrahBaseException;
import ir.kavoshgaran.keycloakauth.service.interfaces.queue.ITokenQueueReceiver;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.Authentication;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.IQueueLogService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.PrematureCloseException;

import java.io.IOException;

@Component
public class TokenQueueReceiver implements ITokenQueueReceiver {

    private Authentication authentication;

    private IQueueLogService queueLogService;

    public TokenQueueReceiver(Authentication authentication, IQueueLogService queueLogService) {
        this.authentication = authentication;
        this.queueLogService = queueLogService;
    }

    /**
     * this method is waiting for get 'token' request message from rabbitmq and return token details if user exists
     * it replies the response through the queue that set in message header
     *
     * @param request
     * @param message
     * @return
     */
    @Override
    @RabbitListener(queues = {"${spring.queue.token}"})
    public GetTokenResponse receive(@Payload GetTokenRequest request, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        if (message.getMessageProperties().getReplyTo() != null) {
            queueLogService.saveRequest(request.toString(), message);
            GetTokenResponse response = new GetTokenResponse();
            try {
                Mono<AccessTokenResponseDto> keycloakResponse = authentication.getToken(new GetTokenDto(request.getUsername(), request.getPassword()));
                AccessTokenResponseDto accessTokenResponseDto = keycloakResponse.block();
                response.setAccessToken(accessTokenResponseDto.getAccessToken());
                response.setRefreshToken(accessTokenResponseDto.getRefreshToken());
                response.setExpireIn(accessTokenResponseDto.getExpireIn());
                response.setExpireInRefresh(accessTokenResponseDto.getExpireInRefresh());
                response.setTokenType(accessTokenResponseDto.getTokenType());
                queueLogService.saveResponse(response.toString(), message);

            } catch (BitrahBaseException e) {
                response.setErrorMessage(e.getMessage());
            } catch (Exception e) {
                channel.basicNack(deliveryTag, false, true);
            }
            return response;
        } else {
            message.getMessageProperties().setReplyTo("x-death");
        }
        return null;
    }
}
