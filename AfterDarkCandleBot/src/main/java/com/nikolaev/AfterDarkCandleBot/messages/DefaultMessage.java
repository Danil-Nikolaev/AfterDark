package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;

@Component
public class DefaultMessage implements Message {

    @Override
    public List<SendMessage> setMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        return List.of(setMessage(chatId, "Выберите одну из команд"));
    }
    
}
