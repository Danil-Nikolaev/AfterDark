package com.nikolaev.AfterDarkCandleBot.messages.interfaces;

import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface Message {
    List<SendMessage> setMessage(Update update);
    
    default SendMessage setMessage(long chatId, String textToSend, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    default SendMessage setMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }
}
