package com.garud.reviewms.review.messaging;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public Queue companyRatingQueue(){
        return new Queue("companyRatingQueue");
    }

    @Bean
    public MessageConverter jsonMessageConvertor(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
          RabbitTemplate rabbitTemplate =new RabbitTemplate(connectionFactory);
             rabbitTemplate.setMessageConverter(jsonMessageConvertor());

        return rabbitTemplate;

    }
}
