package com.example.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitmqApplicationTests {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void msgSend(){

    }
}
