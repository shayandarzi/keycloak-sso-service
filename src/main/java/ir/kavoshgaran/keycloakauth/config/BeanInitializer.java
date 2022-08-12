package ir.kavoshgaran.keycloakauth.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class BeanInitializer {

    @Value("${spring.queue.exchange}")
    private String AUTHENTICATION_EXCHANGE_NAME;

    @Value("${spring.queue.token}")
    private String TOKEN_REQUEST_QUEUE_NAME;

    @Value("${spring.queue.refresh.token}")
    private String REFRESH_TOKEN_REQUEST_QUEUE_NAME;

    @Value("${spring.queue.authenticate.token}")
    private String AUTHENTICATE_TOKEN_REQUEST_QUEUE_NAME;

    @Value("${spring.queue.register}")
    private String REGISTER_REQUEST_QUEUE_NAME;

    @Value("${spring.auth.routing.key}")
    private String AUTHENTICATION_ROUTING_KEY_NAME;

    @Value("${spring.refresh.routing.key}")
    private String REFRESH_TOKEN_ROUTING_KEY_NAME;

    @Value("${spring.authenticate.token.routing.key}")
    private String AUTHENTICATION_TOKEN_ROUTING_KEY_NAME;

    @Value("${spring.register.routing.key}")
    private String REGISTER_ROUTING_KEY_NAME;

    @Value("${spring.queue.reply}")
    private String TOKEN_RESPONSE_QUEUE_NAME;

    @Value("${spring.refresh.queue.reply}")
    private String REFRESH_TOKEN_RESPONSE_QUEUE_NAME;

    @Value("${spring.authenticate.token.queue.reply}")
    private String AUTHENTICATE_TOKEN_RESPONSE_QUEUE_NAME;

    @Value("${spring.register.queue.reply}")
    private String REGISTER_RESPONSE_QUEUE_NAME;

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
                                .doOnConnected(conn -> conn
                                        .addHandlerLast(new ReadTimeoutHandler(60))
                                        .addHandlerLast(new WriteTimeoutHandler(15))));
        ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
        return WebClient.builder().clientConnector(connector).build();
    }

    /**
     * creat authentication rabbitmq exchange bean
     *
     * @return
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(AUTHENTICATION_EXCHANGE_NAME);
    }

    /**
     * building get token queue
     *
     * @return
     */
    @Bean
    Queue tokenRequestQueue() {
        return QueueBuilder.durable(TOKEN_REQUEST_QUEUE_NAME).build();
    }


    /**
     * create response get token queue
     *
     * @return Queue
     */
    @Bean
    Queue tokenResponseQueue() {
        return QueueBuilder.durable(TOKEN_RESPONSE_QUEUE_NAME).build();
    }

    /**
     * binding get token queue to exchange
     *
     * @return
     */
    @Bean
    Binding tokenRequestBinding() {
        return BindingBuilder.bind(tokenRequestQueue()).to(exchange()).with(AUTHENTICATION_ROUTING_KEY_NAME);
    }

    /**
     * building get token by refresh token queue
     *
     * @return
     */
    @Bean
    Queue refreshTokenRequestQueue() {
        return QueueBuilder.durable(REFRESH_TOKEN_REQUEST_QUEUE_NAME).build();
    }

    /**
     * create get token by refresh token response queue
     *
     * @return Queue
     */
    @Bean
    Queue refreshTokenResponseQueue() {

        return QueueBuilder.durable(REFRESH_TOKEN_RESPONSE_QUEUE_NAME).build();
    }

    /**
     * binding get token by refresh token queue to exchange
     *
     * @return
     */
    @Bean
    Binding refreshTokenRequestBinding() {
        return BindingBuilder.bind(refreshTokenRequestQueue()).to(exchange()).with(REFRESH_TOKEN_ROUTING_KEY_NAME);
    }

    /**
     * building authentication token queue
     *
     * @return
     */
    @Bean
    Queue authenticateTokenRequestQueue() {
        return QueueBuilder.durable(AUTHENTICATE_TOKEN_REQUEST_QUEUE_NAME).build();
    }

    /**
     * create authentication token response queue
     *
     * @return
     */
    @Bean
    Queue authenticateTokenResponseQueue() {
        return QueueBuilder.durable(AUTHENTICATE_TOKEN_RESPONSE_QUEUE_NAME).build();
    }

    /**
     * binding authentication token queue to exchange
     *
     * @return
     */
    @Bean
    Binding authenticateTokenRequestBinding() {
        return BindingBuilder.bind(authenticateTokenRequestQueue()).to(exchange()).with(AUTHENTICATION_TOKEN_ROUTING_KEY_NAME);
    }

    /**
     * building register queue
     *
     * @return
     */
    @Bean
    Queue registerRequestQueue() {
        return QueueBuilder.durable(REGISTER_REQUEST_QUEUE_NAME).build();
    }


    /**
     * create register response queue
     *
     * @return Queue
     */
    @Bean
    Queue registerResponseQueue() {
        return QueueBuilder.durable(REGISTER_RESPONSE_QUEUE_NAME).build();
    }

    /**
     * binding register queue to exchange
     *
     * @return
     */
    @Bean
    Binding registerRequestBinding() {
        return BindingBuilder.bind(registerRequestQueue()).to(exchange()).with(REGISTER_ROUTING_KEY_NAME);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
