package ir.kavoshgaran.keycloakauth.service.impl.queue;

import com.rabbitmq.client.Channel;
import ir.kavoshgaran.keycloakauth.dto.rest.AccessTokenResponseDto;
import ir.kavoshgaran.keycloakauth.dto.rest.GetTokenByRefreshTokenDto;
import ir.kavoshgaran.keycloakauth.dto.queue.GetTokenResponse;
import ir.kavoshgaran.keycloakauth.dto.queue.RefreshTokenRequest;
import ir.kavoshgaran.keycloakauth.exception.BitrahBaseException;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.Authentication;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.IQueueLogService;
import ir.kavoshgaran.keycloakauth.service.interfaces.queue.IRefreshTokenQueueReceiver;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;

@Component
@Validated
public class RefreshTokenQueueReceiver implements IRefreshTokenQueueReceiver {

    private final Authentication authentication;

    private IQueueLogService queueLogService;

    public RefreshTokenQueueReceiver(Authentication authentication, IQueueLogService queueLogService) {
        this.authentication = authentication;
        this.queueLogService = queueLogService;
    }

    /**
     * this method is waiting for get 'refresh_token' request message from rabbitmq and return a new access_token
     * it replies the response through the queue that set in message header
     *
     * @param request
     * @param message
     * @return
     */
    @Override
    @RabbitListener(queues = {"${spring.queue.refresh.token}"})
    public GetTokenResponse receive(@Valid @Payload RefreshTokenRequest request, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
        if (message.getMessageProperties().getReplyTo() != null) {

            queueLogService.saveRequest(request.toString(), message);
            GetTokenResponse response = new GetTokenResponse();
            try {
                Mono<AccessTokenResponseDto> keycloakResponse = authentication.getTokenByRefreshToken(new GetTokenByRefreshTokenDto(request.getRefreshToken()));
                AccessTokenResponseDto accessTokenResponseDto = keycloakResponse.block();
                response.setAccessToken(accessTokenResponseDto.getAccessToken());
                response.setRefreshToken(accessTokenResponseDto.getRefreshToken());
                response.setExpireIn(accessTokenResponseDto.getExpireIn());
                response.setExpireInRefresh(accessTokenResponseDto.getExpireInRefresh());
                response.setTokenType(accessTokenResponseDto.getTokenType());
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
}
