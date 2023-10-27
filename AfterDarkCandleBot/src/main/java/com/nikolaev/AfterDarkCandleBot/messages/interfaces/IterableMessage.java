package com.nikolaev.AfterDarkCandleBot.messages.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IterableMessage {
    SendMessage next(Update update);

    SendMessage previous(Update update);
}
