package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Deletable;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;
import com.nikolaev.AfterDarkCandleBot.service.InlineKeyboardFactory;

@Component
public class BasketMessage implements Message, Deletable {

    @Autowired
    private AfterDarkAPI afterDarkAPI;

    @Autowired
    InlineKeyboardFactory inlineKeyboardFactory;

    @Override
    public List<SendMessage> setMessage(Update update) {
        List<SendMessage> result = new ArrayList<>();
        long chatId = update.getMessage().getChatId();

        if (!this.afterDarkAPI.findUser(chatId)) {
            result.add(setMessage(chatId, "Для Начала выполните команду /start"));
            return result;
        }

        ArrayNode candles = this.afterDarkAPI.getCandlesInBasket(chatId);
        if (candles.isEmpty()) {
            result.add(setMessage(chatId, "Пока что у вас пустая корзина"));
        } else {
            result = setListCandleInBusket(chatId, candles);
        }

        return result;
    }

    

    @Override
    public SendMessage delete(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String candleId = parts[1];

        this.afterDarkAPI.deleteCandleFromBasket(chatId, candleId);
        String textToSend = "Вы успешно удалили свечку!!!";
        return setMessage(chatId, textToSend);
    }



    private List<SendMessage> setListCandleInBusket(long chatId, ArrayNode candles) {
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode candle : candles) {
            String textToSend = setCandleMessage(candle);
            String id = candle.get("id").asText();
            InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.deleteInlineKeyboardMarkup(id);

            result.add(setMessage(chatId, textToSend, markupInline));
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
        String textToSend = "Название: " + name +
                "\nФорма: " + shapeName +
                "\nЦвет: " + colorName +
                "\nФитиль: " + wickName +
                "\nАромат: " + smellName +
                "\nЦена: " + price;
        return textToSend;
    }

}
