package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.nikolaev.AfterDarkCandleBot.service.AfterDarkAPI;

public class StartCommandMessage implements Message {

    @Autowired
    private AfterDarkAPI afterDarkAPI;

    @Override
    public List<SendMessage> setMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        String name = update.getMessage().getChat().getFirstName();

        if (!this.afterDarkAPI.findUser(chatId))
            this.afterDarkAPI.registerNewUser(chatId, name);

        String textToSend = "Здравствуйте,  " + name + ", рады Вас видить в нашем интернет магазине.\n";
        textToSend += "Чтобы посмотреть команды бота, выполните команду /help.";

        return List.of(setMessage(chatId, textToSend));
    }

}
