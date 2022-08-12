package ir.kavoshgaran.keycloakauth.service.impl.rest;

import ir.kavoshgaran.keycloakauth.entity.queue.QueueLog;
import ir.kavoshgaran.keycloakauth.entity.queue.QueueMessageType;
import ir.kavoshgaran.keycloakauth.repository.queue.IQueueLogRepo;
import ir.kavoshgaran.keycloakauth.service.interfaces.rest.IQueueLogService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;

@Service
public class QueueLogService implements IQueueLogService {

    private IQueueLogRepo queueLogRepo;

    public QueueLogService(IQueueLogRepo queueLogRepo) {
        this.queueLogRepo = queueLogRepo;
    }

    @Override
    public void saveRequest(String payload, Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        QueueLog log = new QueueLog(payload, messageProperties.getReceivedRoutingKey(), messageProperties.getReplyTo(), messageProperties.getCorrelationId(), messageProperties.getConsumerQueue(), messageProperties.getReceivedExchange(), QueueMessageType.REQUEST);
        queueLogRepo.save(log);
    }

    @Override
    public void saveResponse(String payload, Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        QueueLog log = new QueueLog(payload, messageProperties.getReplyTo(), null, messageProperties.getCorrelationId(), messageProperties.getReplyTo(), "default_exchange", QueueMessageType.RESPONSE);
        queueLogRepo.save(log);
    }
}
