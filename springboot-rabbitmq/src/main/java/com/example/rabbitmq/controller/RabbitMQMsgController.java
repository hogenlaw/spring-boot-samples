package com.example.rabbitmq.controller;

import com.example.rabbitmq.constants.DelayTypeEnum;
import com.example.rabbitmq.mq.DelayMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

@Slf4j
@RequestMapping("rabbitmq")
@RestController
public class RabbitMQMsgController {

    @Resource
    private DelayMessageSender sender;

    /**
     * http://localhost:8080/rabbitmq/sendmsg?msg=HelloWorld&delayType=2
     * 第一条消息在6s后变成了死信消息，然后被消费者消费掉，
     * 第二条消息在30s之后变成了死信消息，然后被消费掉，这样，一个还算ok的延时队列就打造完成了。
     *
     * 问题来了，假如我要它60s之后也变成死信消息，按照这个逻辑，岂不是又要增加一个队列？
     * 如果这样使用的话，岂不是每增加一个新的时间需求，就要新增一个队列，
     * 这里只有6s和60s两个时间选项，如果需要一个小时后处理，那么就需要增加TTL为一个小时的队列，
     * 如果是预定会议室然后提前通知这样的场景，岂不是要增加无数个队列才能满足需求？？
     */
    @RequestMapping("sendmsg")
    public void sendMsg(String msg, Integer delayType){
        log.info("当前时间：{},收到请求，msg:{},delayType:{}", new Date(), msg, delayType);
        sender.sendMsg(msg, Objects.requireNonNull(DelayTypeEnum.getDelayTypeEnumByValue(delayType)));
    }

    /**
     * 基于上面的问题，我们进行优化
     *http://localhost:8080/rabbitmq/delayMsg?msg=操蛋&delayTime=10000 单位ms
     * delayTime 这里可以随意更改，用的都是同一个队列和key，
     *
     * 看起来似乎没什么问题，但不要高兴的太早，在最开始的时候，就介绍过，
     * 如果使用在消息属性上设置TTL的方式，消息可能并不会按时死亡，
     * 因为 RabbitMQ 只会检查第一个消息是否过期，如果过期则丢到死信队列，
     * 索引如果第一个消息的延时时长很长，而第二个消息的延时时长很短，则第二个消息并不会优先得到执行。
     * 就像下面这样：20秒的消息没有得到优先执行
     * 当前时间：Fri Mar 27 17:17:15 CST 2020,收到请求，msg:60秒的消息,delayTime:60000
     * 当前时间：Fri Mar 27 17:17:26 CST 2020,收到请求，msg:20秒的消息,delayTime:20000
     * 当前时间：Fri Mar 27 17:18:15 CST 2020,死信队列C收到消息：60秒的消息
     * 当前时间：Fri Mar 27 17:18:15 CST 2020,死信队列C收到消息：20秒的消息
     *
     * 在设置的TTL时间及时死亡，却无法及时得到消费，就无法设计成一个通用的延时队列。
     *
     * 那如何解决这个问题呢？不要慌，安装一个插件
     */
    @RequestMapping("delayMsg")
    public void delayMsg(String msg, Integer delayTime) {
        log.info("当前时间：{},收到请求，msg:{},delayTime:{}", new Date(), msg, delayTime);
        sender.sendMsg(msg, delayTime);
    }

    /**
     * 这个就是使用插件的方式设计延时队列，可以看到，第二个消息被先消费掉了，符合预期
     *当前时间：Fri Mar 27 17:17:15 CST 2020,收到请求，msg:60秒的消息,delayTime:60000
     * 当前时间：Fri Mar 27 17:17:26 CST 2020,收到请求，msg:20秒的消息,delayTime:20000
     * 当前时间：Fri Mar 27 17:18:15 CST 2020,死信队列C收到消息：20秒的消息
     * 当前时间：Fri Mar 27 17:18:15 CST 2020,死信队列C收到消息：60秒的消息
     */
    @RequestMapping("delayMsg2")
    public void delayMsg2(String msg, Integer delayTime) {
        log.info("当前时间：{},收到请求，msg:{},delayTime:{}", new Date(), msg, delayTime);
        sender.sendDelayMsg(msg, delayTime);
    }
}
