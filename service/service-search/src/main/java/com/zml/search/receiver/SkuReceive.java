package com.zml.search.receiver;


import com.rabbitmq.client.Channel;
import com.zml.mq.constant.MqConst;
import com.zml.search.service.SkuService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.io.IOException;
import java.util.Objects;

/**
 * @author ZhangMinlei
 * @description
 * @date 2023-07-13 20:01
 */
@Component
public class SkuReceive {

    @Resource
    private SkuService skuService;

//    商品上架
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_GOODS_UPPER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT), key = {MqConst.ROUTING_GOODS_UPPER}))
    public void upperSku(Long skuId, Message message, Channel channel) {
        if (!Objects.isNull(skuId)) {
            skuService.upperSku(skuId);
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    商品下架
@RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_GOODS_LOWER, durable = "true"),
        exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT), key = {MqConst.ROUTING_GOODS_LOWER}))
public void lowerSku(Long skuId, Message message, Channel channel) {
    if (!Objects.isNull(skuId)) {
        skuService.lowerSku(skuId);
    }
    try {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
}

    //    商品删除
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = MqConst.QUEUE_GOODS_DELETE, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT), key = {MqConst.ROUTING_GOODS_DELETE}))
    public void deleteSku(Long skuId, Message message, Channel channel) {
        if (!Objects.isNull(skuId)) {
            skuService.deleteById(skuId);
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}


