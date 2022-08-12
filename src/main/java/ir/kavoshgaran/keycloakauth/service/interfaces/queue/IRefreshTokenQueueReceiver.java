package ir.kavoshgaran.keycloakauth.service.interfaces.queue;

import com.rabbitmq.client.Channel;
import ir.kavoshgaran.keycloakauth.dto.queue.GetTokenResponse;
import ir.kavoshgaran.keycloakauth.dto.queue.RefreshTokenRequest;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

public interface IRefreshTokenQueueReceiver {

    GetTokenResponse receive(@Payload RefreshTokenRequest request, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException;
}
