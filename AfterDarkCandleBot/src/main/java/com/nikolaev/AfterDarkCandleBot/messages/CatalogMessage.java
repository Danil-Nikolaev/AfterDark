package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.IterableMessage;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;
import com.nikolaev.AfterDarkCandleBot.service.InlineKeyboardFactory;

@Component
public class CatalogMessage implements Message, IterableMessage, BeingAdded {

    @Autowired
    private AfterDarkAPI afterDarkAPI;
    @Autowired
    private InlineKeyboardFactory inlineKeyboardFactory;
    @Autowired
    private MessageIterator iterator;

    private long chatId;

    @Override
    public List<SendMessage> setMessage(Update update) {
        this.chatId = update.getMessage().getChatId();
        if (!this.afterDarkAPI.findUser(chatId))
            return List.of(setMessage(chatId, "Для Начала выполните команду /start"));

        ArrayNode candles = getAll();

        this.iterator.createIterator(candles);

        return List.of(next(update));

    }

    @Override
    public SendMessage next(Update update) {
        String id;

        JsonNode candle = this.iterator.next();
        String textToSend = setCandleMessage(candle);
        id = candle.get("id").asText();
        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addInBasketInlineKeyboardMarkup(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage previous(Update update) {
        String id;

        JsonNode candle = this.iterator.previous();
        id = candle.get("id").asText();
        String textToSend = setCandleMessage(candle);
        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addInBasketInlineKeyboardMarkup(id);

        return setMessage(chatId, textToSend, markupInline);
    }


    @Override
    public SendMessage add(Update update) {
        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String candleId = parts[1];
        
        this.afterDarkAPI.addCandleInBasket(chatId, candleId);
        String textToSend = "Вы добавили новую свечу!";
        return setMessage(chatId, textToSend);
    }

    private ArrayNode getAll() {
        return this.afterDarkAPI.getCandles(chatId);
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
