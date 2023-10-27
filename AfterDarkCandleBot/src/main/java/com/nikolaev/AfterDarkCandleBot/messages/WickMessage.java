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
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.IterableMessage;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.models.Candle;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;
import com.nikolaev.AfterDarkCandleBot.service.CandleService;
import com.nikolaev.AfterDarkCandleBot.service.InlineKeyboardFactory;

@Component
public class WickMessage implements Message, IterableMessage, BeingAdded {

    @Autowired
    private AfterDarkAPI afterDarkAPI;
    
    @Autowired
    private MessageIterator messageIterator;
    
    @Autowired
    private InlineKeyboardFactory inlineKeyboardFactory;

    @Autowired
    private CandleService candleService;

    @Override
    public List<SendMessage> setMessage(Update update) {
        ArrayNode wicks = this.afterDarkAPI.getAllWick();
        long chatId;
        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }        this.messageIterator.createIterator(wicks);

        Candle candle = this.candleService.findByChatId(chatId);
        if (candle == null) this.candleService.save(chatId, new Candle(chatId));

        List<SendMessage> result = new ArrayList<>();
        result.add(setMessage(chatId, "Отлично теперь выберите фитиль"));
        result.add(next(update));
        return result;
    }

    @Override
    public SendMessage next(Update update) {
        long chatId;
        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }        JsonNode wick = this.messageIterator.next();
        String name = wick.get("name").asText();
        String id = wick.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomWick(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage previous(Update update) {
        long chatId;
        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }        JsonNode wick = this.messageIterator.previous();
        String name = wick.get("name").asText();
        String id = wick.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomWick(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage add(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String wickId = parts[1];

        this.candleService.updateWickIdByChatId(chatId, Long.valueOf(wickId));

        String textToSend = "Отлично вы успешно выбрали фитиль!";
        return setMessage(chatId, textToSend);
    }
    

}
