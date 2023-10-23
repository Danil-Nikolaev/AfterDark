package com.nikolaev.AfterDarkCandleBot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
public class ReplyKeyboardFactory {

    public ReplyKeyboardMarkup mainKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Каталог");
        row1.add("Ваша корзина");
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Сделать кастомную свечу");
        row2.add("Посмотреть свои заказы");
        List<KeyboardRow> listKeyboardRows = new ArrayList<>();
        listKeyboardRows.add(row1);
        listKeyboardRows.add(row2);

        keyboardMarkup.setKeyboard(listKeyboardRows);
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup basketKeyboardMarkup() {
    
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        KeyboardRow rowReply1 = new KeyboardRow();
        rowReply1.add("Оформить заказ");
        KeyboardRow rowReply2 = new KeyboardRow();
        rowReply2.add("На главную страницу");

        List<KeyboardRow> listKeyboardRows = new ArrayList<>();
        listKeyboardRows.add(rowReply1);
        listKeyboardRows.add(rowReply2);

        keyboardMarkup.setKeyboard(listKeyboardRows);
        return keyboardMarkup;
    }
}
