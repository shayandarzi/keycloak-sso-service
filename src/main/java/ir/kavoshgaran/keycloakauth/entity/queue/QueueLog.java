package ir.kavoshgaran.keycloakauth.entity.queue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_queue_log")
public class QueueLog extends BaseEntity {

    @Column(name = "payload", length = 2048, updatable = false)
    private String payload;

    @Column(name = "routing_key")
    private String routingKey;

    @Column(name = "reply_to")
    private String replyTo;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "consumer_queue")
    private String consumerQueue;

    @Column(name = "exchange_name")
    private String exchange;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 15)
    private QueueMessageType type;

}
