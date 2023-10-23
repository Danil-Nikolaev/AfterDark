package com.nikolaev.AfterDarkCandleBot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class InlineKeyboardFactory {

    public InlineKeyboardMarkup deleteInlineKeyboardMarkup(String id) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
        buttonAddInBasket.setText("Удалить");
        buttonAddInBasket.setCallbackData("deleteCandleFromBasket:" + id);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonAddInBasket);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup addInBasketInlineKeyboardMarkup(String id) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
        buttonAddInBasket.setText("Добавить в корзину");
        buttonAddInBasket.setCallbackData("addCandleInBasket:" + id);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonAddInBasket);

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("Дальше");
        nextButton.setCallbackData("nextCandleInCatalog:");

        InlineKeyboardButton previousButton = new InlineKeyboardButton();
        previousButton.setText("Назад");
        previousButton.setCallbackData("previousCandleInCatalog:");

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(previousButton);
        row2.add(nextButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup addKeyboardMarkupForCustomShape(String id) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
        buttonAddInBasket.setText("Выбрать форму");
        buttonAddInBasket.setCallbackData("addShapeInCandle:" + id);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonAddInBasket);

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("Дальше");
        nextButton.setCallbackData("nextShapeInCustomCandle:");

        InlineKeyboardButton previousButton = new InlineKeyboardButton();
        previousButton.setText("Назад");
        previousButton.setCallbackData("previousShapeInCustomCandle:");

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(previousButton);
        row2.add(nextButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup addKeyboardMarkupForCustomSmell(String id) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
        buttonAddInBasket.setText("Выбрать форму");
        buttonAddInBasket.setCallbackData("addSmellInCandle:" + id);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonAddInBasket);

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("Дальше");
        nextButton.setCallbackData("nextSmellInCustomCandle:");

        InlineKeyboardButton previousButton = new InlineKeyboardButton();
        previousButton.setText("Назад");
        previousButton.setCallbackData("previousSmellInCustomCandle:");

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(previousButton);
        row2.add(nextButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup addKeyboardMarkupForCustomWick(String id) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
        buttonAddInBasket.setText("Выбрать форму");
        buttonAddInBasket.setCallbackData("addWickInCandle:" + id);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonAddInBasket);

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("Дальше");
        nextButton.setCallbackData("nextWickInCustomCandle:");

        InlineKeyboardButton previousButton = new InlineKeyboardButton();
        previousButton.setText("Назад");
        previousButton.setCallbackData("previousWickInCustomCandle:");

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(previousButton);
        row2.add(nextButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public InlineKeyboardMarkup addKeyboardMarkupForCustomColor(String id) {

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
        buttonAddInBasket.setText("Выбрать форму");
        buttonAddInBasket.setCallbackData("addColorInCandle:" + id);

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(buttonAddInBasket);

        InlineKeyboardButton nextButton = new InlineKeyboardButton();
        nextButton.setText("Дальше");
        nextButton.setCallbackData("nextColorInCustomCandle:");

        InlineKeyboardButton previousButton = new InlineKeyboardButton();
        previousButton.setText("Назад");
        previousButton.setCallbackData("previousColorInCustomCandle:");

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(previousButton);
        row2.add(nextButton);

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
