package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.service.OrderService;

@Component
public class PhoneMessage implements Message, BeingAdded {

    @Autowired
    private OrderService orderService;

    @Override
    public List<SendMessage> setMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        return List.of(setMessage(chatId, "Теперь напишите свой номер телефона"));
    }

    @Override
    public SendMessage add(Update update) {
        long chatId = update.getMessage().getChatId();
        String phone = update.getMessage().getText();
        this.orderService.updatePhone(chatId, phone);
        return setMessage(chatId, "Отлично, Вы добавили номер телефона");
    }
    
}
