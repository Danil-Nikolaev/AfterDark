package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.models.Candle;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;
import com.nikolaev.AfterDarkCandleBot.service.CandleService;

@Component
public class CustomCandleMessage implements Message, BeingAdded {
    
    @Autowired
    AfterDarkAPI afterDarkAPI;

    @Autowired
    private CandleService candleService;

    @Override
    public List<SendMessage> setMessage(Update update) {
        long chatId;

        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        return List.of(setMessage(chatId, "поздравляю вы создали свечу"));
    }

    @Override
    public SendMessage add(Update update) {
        long chatId;

        if (update.getCallbackQuery() == null) {
            chatId = update.getMessage().getChatId();
        } else {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        }

        Candle candle = this.candleService.findByChatId(chatId);
        Map<String, Long> candleMap = new HashMap<>();
        candleMap.put("colorShape", candle.getColorShapeId());
        candleMap.put("shape", candle.getShapeId());
        candleMap.put("smell", candle.getSmellId()); 
        candleMap.put("wick", candle.getWickId());
        
        this.afterDarkAPI.createNewCandle(chatId, candleMap);
        this.candleService.save(chatId, new Candle(chatId));
        return setMessage(chatId, "поздравляю вы создали свечу");
    }

    
}
