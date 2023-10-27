package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class SmellMessage implements Message, IterableMessage, BeingAdded {

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
        ArrayNode smells = this.afterDarkAPI.getAllSmell();
        long chatId;
        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }
        this.messageIterator.createIterator(smells);

        Candle candle = this.candleService.findByChatId(chatId);
        if (candle == null) this.candleService.save(chatId, new Candle(chatId));

        List<SendMessage> result = new ArrayList<>();
        String textToSend = "Отлично, теперь выберите аромат";
        result.add(setMessage(chatId, textToSend));
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
        }        JsonNode smell = this.messageIterator.next();
        String name = smell.get("name").asText();
        String id = smell.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomSmell(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage previous(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        JsonNode smell = this.messageIterator.previous();
        String name = smell.get("name").asText();
        String id = smell.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomSmell(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage add(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String smellId = parts[1];
        
        this.candleService.updateSmellIdByChatId(chatId, Long.valueOf(smellId));

        String textToSend = "Отлично вы успешно выбрали ароматы!";
        return setMessage(chatId, textToSend);
    }

}
