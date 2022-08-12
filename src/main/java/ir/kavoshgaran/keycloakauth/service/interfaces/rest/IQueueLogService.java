package ir.kavoshgaran.keycloakauth.service.interfaces.rest;

import org.springframework.amqp.core.Message;

public interface IQueueLogService {

    void saveRequest(String payload, Message message);

    void saveResponse(String payload, Message message);
}
