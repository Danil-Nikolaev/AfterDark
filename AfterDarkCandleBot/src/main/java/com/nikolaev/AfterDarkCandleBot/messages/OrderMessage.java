package com.nikolaev.AfterDarkCandleBot.messages;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.models.Order;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;
import com.nikolaev.AfterDarkCandleBot.service.OrderService;

@Component
public class OrderMessage implements Message, BeingAdded {

    @Autowired
    private AfterDarkAPI afterDarkAPI;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public List<SendMessage> setMessage(Update update) {
        return null;
    }

    @Override
    public SendMessage add(Update update) {
        long chatId = update.getMessage().getChatId();

        Order order = this.orderService.findByChatId(chatId);

        String numberPhone = order.getPhone();
        String address = order.getAddress();

        ObjectNode orderObjectNode = objectMapper.createObjectNode();

        orderObjectNode.put("purchaseService", "TELEGRAM");
        orderObjectNode.put("pamentMethod", "CARDCURIER");
        orderObjectNode.put("stageOfWork", "ORDERED");
        orderObjectNode.put("communication", numberPhone);
        orderObjectNode.put("address", address);
        orderObjectNode.put("paid", false);
        orderObjectNode.put("dateOfPurchase", LocalDateTime.now().toString());
        orderObjectNode.put("dateOfDelivery", "null");
        JsonNode user = this.afterDarkAPI.findUserByLogin(chatId);
        long userId = Long.valueOf(user.get("id").asText());
        JsonNode basket = this.afterDarkAPI.findBasketById(userId);
        ArrayNode candles = (ArrayNode) basket.get("candles");
        int price = 0;
        for (JsonNode candle : candles) {
            int localPrice = candle.get("price").asInt();
            price += localPrice;
        }
        orderObjectNode.put("price", price);
        orderObjectNode.set("user", user);
        orderObjectNode.set("candles", candles);

        JsonNode newOrder = this.afterDarkAPI.createNewOrder(orderObjectNode);
        String textToSend = "Поздравляю вы оформили заказ";
        this.afterDarkAPI.deleteCandlesInBusket(chatId);
        if (newOrder == null)
            textToSend = "К сожалению не получилось оформить заказ, попробуйте снова!";

        return setMessage(chatId, textToSend);
    }
    
}
