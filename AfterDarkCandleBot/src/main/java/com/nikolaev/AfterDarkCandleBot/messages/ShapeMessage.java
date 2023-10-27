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
public class ShapeMessage implements Message, IterableMessage, BeingAdded {

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
        List<SendMessage> result;
        long chatId = update.getMessage().getChatId();

        if (!this.afterDarkAPI.findUser(chatId)) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Для Начала выполните команду /start"));
            return result;
        }

        ArrayNode shapes = this.afterDarkAPI.getAllShapes();
        this.messageIterator.createIterator(shapes);

        Candle candle = this.candleService.findByChatId(chatId);
        if (candle == null) this.candleService.save(chatId, new Candle(chatId));

        result = new ArrayList<>();
        String textToSend = "Отлично, для начала выберите номер формы";
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
        }
        
        JsonNode shape = this.messageIterator.next();
        String name = shape.get("name").asText();
        String id = shape.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomShape(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage previous(Update update) {
        long chatId;
        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }        JsonNode shape = this.messageIterator.previous();

        String name = shape.get("name").asText();
        String id = shape.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomShape(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    @Override
    public SendMessage add(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        
        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String shapeId = parts[1];

        this.candleService.updateShapeIdByChatId(chatId, Long.valueOf(shapeId));

        String textToSend = "Отлично вы успешно выбрали форму!";
        return setMessage(chatId, textToSend);
    }
    
}
