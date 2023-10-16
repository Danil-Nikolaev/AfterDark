package com.nikolaev.AfterDarkCandleBot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class Message {

    private AfterDarkAPI afterDarkAPI;
    private ObjectMapper objectMapper;
    private Map<Long, Map<String, Long>> candleMaps;
    private Map<Long, Map<String, String>> orderMaps;

    public Message(@Autowired AfterDarkAPI afterDarkAPI, @Autowired ObjectMapper objectMapper) {
        this.afterDarkAPI = afterDarkAPI;
        this.objectMapper = objectMapper;
        this.candleMaps = new HashMap<>();
        this.orderMaps = new HashMap<>();
    }

    /*
     * This is method generate message on command /start
     */
    public SendMessage startMessage(long chatId, String name) {
        if (!this.afterDarkAPI.findUser(chatId))
            this.afterDarkAPI.registerNewUser(chatId, name);

        String textToSend = "Здравствуйте,  " + name + ", рады вас видить в нашем интернет магазине.\n";

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

        return setMessage(chatId, textToSend, keyboardMarkup);
    }

    /*
     * This is method generate message with catalog candles
     * on message "Каталог"
     */
    public List<SendMessage> catalogMessage(long chatId) {
        if (!this.afterDarkAPI.findUser(chatId))
            return null;

        ArrayNode candles = this.afterDarkAPI.getCandles(chatId);

        return setListCatalog(chatId, candles);
    }

    /*
     * This is method generate simple message with only text
     */
    public SendMessage defualtMessage(long chatId, String textToSend) {
        return setMessage(chatId, textToSend);
    }

    /*
     * This is method add candle in basket, then send accept message
     */
    public SendMessage addInBasketMessage(long chatId, String candleId) {
        this.afterDarkAPI.addCandleInBasket(chatId, candleId);
        String textToSend = "Вы добавили новую свечку)";
        return setMessage(chatId, textToSend);
    }

    /*
     * This is method generate message with candles in basket user
     */
    public List<SendMessage> listCandlesInBasket(long chatId) {
        if (!this.afterDarkAPI.findUser(chatId)) {
            return null;
        }

        ArrayNode candles = this.afterDarkAPI.getCandlesInBasket(chatId);
        List<SendMessage> result = setListCandleInBusket(chatId, candles);

        return result;
    }

    /*
     * This is method in start delete candle,
     * then generate message about success delete 
     */
    public SendMessage deleteCandleFromMessage(long chatId, String candleId) {
        this.afterDarkAPI.deleteCandleFromBasket(chatId, candleId);
        String textToSend = "Вы успешно удалили свечку!!!";
        return setMessage(chatId, textToSend);
    }

    /*
     * This is method generate message with all shape
     */
    public List<SendMessage> makeCustomCandle(long chatId) {
        ArrayNode shapes = this.afterDarkAPI.getAllShapes();
        int shapeId = 1;
        List<SendMessage> result = new ArrayList<>();
        this.candleMaps.put(chatId, new HashMap<String, Long>());
        for (JsonNode shape : shapes) {
            String name = shape.get("name").asText();
            String textToSend = String.valueOf(shapeId) + ". " + name;
            shapeId++;
            result.add(setMessage(chatId, textToSend));
        }
        String textToSend = "Отлично, для начала выберите номер формы";
        result.add(setMessage(chatId, textToSend));
        return result;
    }

    /*
     * This is method set shape in local candle
     */
    public SendMessage setShape(long chatId, long shapeId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("shape", shapeId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали форму!";
        return setMessage(chatId, textToSend);
    }

    /*
     * this is method generate message with all smell
     */
    public List<SendMessage> getAllSmell(long chatId) {
        ArrayNode smells = this.afterDarkAPI.getAllSmell();
        int smellId = 1;
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode smell : smells) {
            String name = smell.get("name").asText();
            String textToSend = String.valueOf(smellId) + ". " + name;
            smellId++;
            result.add(setMessage(chatId, textToSend));
        }

        return result;
    }

    public SendMessage setSmell(long chatId, long smellId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("smell", smellId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали ароматы!";
        return setMessage(chatId, textToSend);
    }

    public List<SendMessage> getAllColor(long chatId) {
        ArrayNode colors = this.afterDarkAPI.getAllColor();
        int colorId = 1;
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode color : colors) {
            String name = color.get("name").asText();
            String textToSend = String.valueOf(colorId) + ". " + name;
            colorId++;
            result.add(setMessage(chatId, textToSend));
        }

        return result;
    }

    public SendMessage setColor(long chatId, long colorId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("colorShape", colorId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали Цвет!";
        return setMessage(chatId, textToSend);
    }

    public List<SendMessage> getAllWick(long chatId) {
        ArrayNode wicks = this.afterDarkAPI.getAllWick();
        int wickId = 1;
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode wick : wicks) {
            String name = wick.get("name").asText();
            String textToSend = String.valueOf(wickId) + ". " + name;
            wickId++;
            result.add(setMessage(chatId, textToSend));
        }

        return result;
    }

    public SendMessage setWick(long chatId, long wickId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("wick", wickId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали фитиль!";
        return setMessage(chatId, textToSend);
    }

    public SendMessage setCandle(long chatId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        this.afterDarkAPI.createNewCandle(chatId, candleMap);
        String textToSend = "Поздравляю, вы создали свою свечку";
        return setMessage(chatId, textToSend);
    }

    public SendMessage startSetOrder(long chatId) {
        this.orderMaps.put(chatId, new HashMap<String, String>());
        String textToSend = "Отично, для начала введите своё имя";
        return setMessage(chatId, textToSend);
    }

    public SendMessage setName(long chatId, String name) {
        Map<String, String> order = this.orderMaps.get(chatId);
        order.put("name", name);
        this.orderMaps.put(chatId, order);
        String textToSend = "Отлично";
        return setMessage(chatId, textToSend);
    }

    public SendMessage getPhone(long chatId) {
        String textToSend = "Теперь напишите свой номер телефона";
        return setMessage(chatId, textToSend);
    }

    public SendMessage setPhone(long chatId, String phone) {
        Map<String, String> order = this.orderMaps.get(chatId);
        order.put("phone", phone);
        this.orderMaps.put(chatId, order);
        String textToSend = "Отлично";
        return setMessage(chatId, textToSend);
    }

    public SendMessage getAddress(long chatId) {
        String textToSend = "Теперь напишите свой адрес";
        return setMessage(chatId, textToSend);
    }

    public SendMessage setAddress(long chatId, String address) {
        Map<String, String> order = this.orderMaps.get(chatId);
        order.put("address", address);
        this.orderMaps.put(chatId, order);
        String textToSend = "Отлично";
        return setMessage(chatId, textToSend);
    }

    public SendMessage createNewOrder(long chatId) {
        Map<String, String> orderMap = this.orderMaps.get(chatId);
        String numberPhone = orderMap.get("phone");
        String address = orderMap.get("address");
        // String name = orderMap.get("name");

        ObjectNode order = objectMapper.createObjectNode();

        order.put("purchaseService", "TELEGRAM");
        order.put("pamentMethod", "CARDCURIER");
        order.put("stageOfWork", "ORDERED");
        order.put("communication", numberPhone);
        order.put("address", address);
        order.put("paid", false);
        order.put("dateOfPurchase", LocalDateTime.now().toString());
        order.put("dateOfDelivery", "null");
        JsonNode user = this.afterDarkAPI.findUserByLogin(chatId);
        long userId = Long.valueOf(user.get("id").asText());
        JsonNode basket = this.afterDarkAPI.findBasketById(userId);
        ArrayNode candles = (ArrayNode) basket.get("candles");
        int price = 0;
        for (JsonNode candle : candles) {
            int localPrice = candle.get("price").asInt();
            price += localPrice;
        }
        order.put("price", price);
        order.set("user", user);
        order.set("candles", candles);

        JsonNode newOrder= this.afterDarkAPI.createNewOrder(order);
        String textToSend = "Поздравляю вы оформили заказ";

        if (newOrder == null) textToSend = "К сожалению не получилось оформить заказ, попробуйте снова!";


        return setMessage(chatId, textToSend);
    }

    private List<SendMessage> setListCandleInBusket(long chatId, ArrayNode candles) {
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode candle : candles) {
            String textToSend = setCandleMessage(candle);
            String id = candle.get("id").asText();
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

            InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
            buttonAddInBasket.setText("Удалить");
            buttonAddInBasket.setCallbackData("deleteCandleFromBasket:" + id);

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            row1.add(buttonAddInBasket);

            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            rowsInline.add(row1);

            markupInline.setKeyboard(rowsInline);

            result.add(setMessage(chatId, textToSend, markupInline));
        }
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
        String textToSend = "Хотите ли оформить заказ?";
        result.add(setMessage(chatId, textToSend, keyboardMarkup));
        return result;
    }

    private List<SendMessage> setListCatalog(long chatId, ArrayNode candles) {
        String id;
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode candle : candles) {
            String textToSend = setCandleMessage(candle);
            id = candle.get("id").asText();
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

            InlineKeyboardButton buttonAddInBasket = new InlineKeyboardButton();
            buttonAddInBasket.setText("Добавить в корзину");
            buttonAddInBasket.setCallbackData("addCandleInBasket:" + id);

            List<InlineKeyboardButton> row1 = new ArrayList<>();
            row1.add(buttonAddInBasket);

            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            rowsInline.add(row1);

            markupInline.setKeyboard(rowsInline);
            result.add(setMessage(chatId, textToSend, markupInline));
        }
        return result;
    }

    private String setCandleMessage(JsonNode candle) {
        String name = candle.get("name").asText();
        String description = candle.get("description").asText();
        String quanity = candle.get("quanity").asText();
        String price = candle.get("price").asText();
        String smellName = candle.get("smell").get("name").asText();
        String textToSend = "Название свечи: " + name +
                "\nОписание: " + description +
                "\nКоличество свеч: " + quanity +
                "\nЦена: " + price +
                "\nАромат: " + smellName;
        return textToSend;
    }

    private SendMessage setMessage(long chatId, String textToSend, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    private SendMessage setMessage(long chatId, String textToSend, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    private SendMessage setMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return message;
    }
}
