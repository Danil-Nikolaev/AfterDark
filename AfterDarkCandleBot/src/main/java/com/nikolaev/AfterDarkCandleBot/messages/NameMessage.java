package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;
import com.nikolaev.AfterDarkCandleBot.models.Order;
import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;
import com.nikolaev.AfterDarkCandleBot.service.OrderService;

@Component
public class NameMessage implements Message, BeingAdded{
    
    @Autowired
    private AfterDarkAPI afterDarkAPI;

    @Autowired
    private OrderService orderService;

    @Override
    public List<SendMessage> setMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        if (!this.afterDarkAPI.findUser(chatId)) {
            return List.of(setMessage(chatId, "Для Начала выполните команду /start"));
        }

        ArrayNode candles = this.afterDarkAPI.getCandlesInBasket(chatId);
        if (candles.isEmpty()) {
            return List.of(setMessage(chatId, "Пока что у вас пустая корзина.\nДля начала пополните корзину."));
        }

        Order order = this.orderService.findByChatId(chatId);
        if (order == null) {
            this.orderService.save(chatId, new Order(chatId));
        }
        
        String textToSend = "Отично, для начала введите своё имя";
        return List.of(setMessage(chatId, textToSend));
    }

    @Override
    public SendMessage add(Update update) {
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getText();
        this.orderService.updateName(chatId, name);
        return setMessage(chatId, "Отлично, Вы добавили имя");
    }

    
}
