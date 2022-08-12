package ir.kavoshgaran.keycloakauth.service.impl.queue;

import com.rabbitmq.client.Channel;
import ir.kavoshgaran.keycloakauth.dto.rest.RegisterDto;
import ir.kavoshgaran.keycloakauth.dto.queue.RegisterRequest;
import ir.kavoshgaran.keycloakauth.dto.queue.RegisterResponse;
import ir.kavoshgaran.keycloakauth.exception.BitrahBaseException;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.Authentication;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.IQueueLogService;
import ir.kavoshgaran.keycloakauth.service.interfaces.queue.IRegisterQueueReceiver;
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
public class RegisterQueueReceiver implements IRegisterQueueReceiver {

    private Authentication authentication;

    private IQueueLogService queueLogService;

    public RegisterQueueReceiver(Authentication authentication, IQueueLogService queueLogService) {
        this.authentication = authentication;
        this.queueLogService = queueLogService;
    }

    /**
     * this method is waiting for get 'register' request message from rabbitmq
     * it replies the response through the queue that set in message header
     *
     * @param request
     * @param message
     * @return
     */
    @Override
    @RabbitListener(queues = {"${spring.queue.register}"})
    public RegisterResponse receive(@Payload RegisterRequest request, Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) Long deliveryTag) throws IOException {
//        if (message.getMessageProperties().getReplyTo() != null) {
//            queueLogService.saveRequest(request.toString(), message);
//            RegisterResponse response = new RegisterResponse();
//            try {
//                Mono<Boolean> keycloakResponse = authentication.register(new RegisterDto(request.getUsername(), request.getPassword()));
//                Boolean isSuccess = keycloakResponse.block();
//                if (isSuccess == null)
//                    throw new NotAcceptableException("error on register service");
//                response.setSuccess(isSuccess);
//            } catch (BitrahBaseException e) {
//                response.setErrorMessage(e.getMessage());
//            } catch (Exception e) {
//                channel.basicNack(deliveryTag, false, true);
//            }
//            queueLogService.saveResponse(response.toString(), message);
//            return response;
//        } else {
//            message.getMessageProperties().setReplyTo("x-death");
//        }
        return null;
    }
}
