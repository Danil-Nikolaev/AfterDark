package com.nikolaev.AfterDarkCandleBot.messages;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;

@Component
public class HelpCommandMessage implements Message {

    @Override
    public List<SendMessage> setMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        String textToSend = """
            Команды:
            /start - Запускает телеграм бота.
            /help - Справочная информация о командах бота.
            /basket - Посмотрите корзину, которую вы собрали.
            /catalog - Показывает каталог стандартных свечей.
            /custom - Создать свою собсвенную свечу.
            /order - Офрмить заказ.
            /orders - Посмотреть историю ваших заказов.
            """;
        return List.of(setMessage(chatId, textToSend));
    }
    
}
