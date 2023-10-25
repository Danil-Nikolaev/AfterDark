package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;

public class OrdersMessage implements Message {

    @Autowired
    private AfterDarkAPI afterDarkAPI;

    @Override
    public List<SendMessage> setMessage(Update update) {
        List<SendMessage> result = new ArrayList<>();
        long chatId = update.getMessage().getChatId();

        if (!this.afterDarkAPI.findUser(chatId)) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Для Начала выполните команду /start."));
            return result;
        }
        ArrayNode orders = this.afterDarkAPI.getAllOrdersByUser(chatId);
        
        if (orders.isEmpty()) {
            result.add(setMessage(chatId, "На данный момент у Вас нет заказов"));
            return result;
        } 

        for (JsonNode order : orders) {
            String id = order.get("id").asText();
            result.add(setMessage(chatId, "Номер заказа: " + id + "\nСвечи в заказе"));
            ArrayNode candles = (ArrayNode) order.get("candles");
            for (JsonNode candle : candles) {
                String textToSend = setCandleMessage(candle);
                result.add(setMessage(chatId, textToSend));
            }
        }
        return result;
    }

    private String setCandleMessage(JsonNode candle) {
        Boolean custom = candle.get("custom").asBoolean();
        String name = candle.get("name").asText();
        if (custom) {
            name = "Ваша оригинальная свеча.";
        }
        String price = candle.get("price").asText();
        String wickName = candle.get("wick").get("name").asText();
        String shapeName = candle.get("shape").get("name").asText();
        String colorName = candle.get("colorShape").get("name").asText();
        String smellName = candle.get("smell").get("name").asText();
        String textToSend = 
                "Название: " + name + 
                "\nФорма: " + shapeName +
                "\nЦвет: " + colorName +
                "\nФитиль: " + wickName +
                "\nАромат: " + smellName +
                "\nЦена: " + price;
        return textToSend;
    }
    
}
