package com.nikolaev.AfterDarkCandleBot.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class Message {

    private AfterDarkAPI afterDarkAPI;
    private ObjectMapper objectMapper;
    private InlineKeyboardFactory inlineKeyboardFactory;
    private ReplyKeyboardFactory replyKeyboardFactory;
    private ListIterator<JsonNode> iterator;
    private boolean nextButton = false;
    private boolean previousButton = false;
    private Map<Long, Map<String, Long>> candleMaps;
    private Map<Long, Map<String, String>> orderMaps;

    public Message(@Autowired AfterDarkAPI afterDarkAPI, @Autowired ObjectMapper objectMapper,
            @Autowired ReplyKeyboardFactory replyKeyboardFactory,
            @Autowired InlineKeyboardFactory inlineKeyboardFactory) {
        this.afterDarkAPI = afterDarkAPI;
        this.objectMapper = objectMapper;
        this.inlineKeyboardFactory = inlineKeyboardFactory;
        this.replyKeyboardFactory = replyKeyboardFactory;
        this.candleMaps = new HashMap<>();
        this.orderMaps = new HashMap<>();
    }

    public List<SendMessage> findAllOrdersByUser(long chatId) {
        List<SendMessage> result;
        if (!this.afterDarkAPI.findUser(chatId)) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Для Начала выполните команду /start."));
            return result;
        }
        ArrayNode orders = this.afterDarkAPI.getAllOrdersByUser(chatId);
        if (orders.isEmpty()) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Пока что у вас нет никаких заказов."));
            return result;
        } 
        result = new ArrayList<>();
        for (JsonNode order : orders) {
            String id = order.get("id").asText();
            result.add(setMessage(chatId, "Номер заказа: " + id + "\nСвечи в заказе"));
            ArrayNode candles = (ArrayNode) order.get("candles");
            for (JsonNode candle : candles) {
                String textToSend = setCandleMessage(candle);
                result.add(setMessage(chatId, textToSend));
            }
        }
        return result;
    }

    /*
     * This is method generate message on command /start
     */
    public SendMessage startMessage(long chatId, String name) {
        if (!this.afterDarkAPI.findUser(chatId))
            this.afterDarkAPI.registerNewUser(chatId, name);

        String textToSend = "Здравствуйте,  " + name + ", рады вас видить в нашем интернет магазине.\n";
        textToSend += "Чтобы посмотреть команды бота, выполните команду /help.";

        // ReplyKeyboardMarkup keyboardMarkup =
        // this.replyKeyboardFactory.mainKeyboardMarkup();

        return setMessage(chatId, textToSend);
    }

    /*
     * This is method generate message with catalog candles
     * on message "Каталог"
     */
    public SendMessage catalogMessage(long chatId) {
        if (!this.afterDarkAPI.findUser(chatId))
            return setMessage(chatId, "Для Начала выполните команду /start");

        ArrayNode candles = this.afterDarkAPI.getCandles(chatId);

        this.iterator = createIterator(candles);

        return nextCandleInCatalog(chatId);
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
        List<SendMessage> result;

        if (!this.afterDarkAPI.findUser(chatId)) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Для Начала выполните команду /start"));
            return result;
        }

        ArrayNode candles = this.afterDarkAPI.getCandlesInBasket(chatId);
        if (candles.isEmpty()) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Пока что у вас пустая корзина"));
        } else {
            result = setListCandleInBusket(chatId, candles);
        }

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
    public List<SendMessage> getAllShape(long chatId) {
        List<SendMessage> result;

        if (!this.afterDarkAPI.findUser(chatId)) {
            result = new ArrayList<>();
            result.add(setMessage(chatId, "Для Начала выполните команду /start"));
            return result;
        }

        ArrayNode shapes = this.afterDarkAPI.getAllShapes();
        this.iterator = createIterator(shapes);

        result = new ArrayList<>();
        String textToSend = "Отлично, для начала выберите номер формы";
        result.add(setMessage(chatId, textToSend));
        this.candleMaps.put(chatId, new HashMap<String, Long>());
        result.add(nextShapeInCustomCandle(chatId));

        return result;
    }

    public SendMessage nextShapeInCustomCandle(long chatId) {
        JsonNode shape = next();
        String name = shape.get("name").asText();
        String id = shape.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomShape(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage previousShapeInCustomCandle(long chatId) {
        JsonNode shape = previous();

        String name = shape.get("name").asText();
        String id = shape.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomShape(id);
        return setMessage(chatId, textToSend, markupInline);
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

        this.iterator = createIterator(smells);

        List<SendMessage> result = new ArrayList<>();
        String textToSend = "Отлично, теперь выберите аромат";
        result.add(setMessage(chatId, textToSend));
        result.add(nextSmellInCustomCandle(chatId));
        return result;
    }

    public SendMessage nextSmellInCustomCandle(long chatId) {
        JsonNode smell = next();
        String name = smell.get("name").asText();
        String id = smell.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomSmell(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage previousSmellInCustomCandle(long chatId) {
        JsonNode smell = previous();
        String name = smell.get("name").asText();
        String id = smell.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomSmell(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage setSmell(long chatId, long smellId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("smell", smellId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали ароматы!";
        return setMessage(chatId, textToSend);
    }

    public List<SendMessage> getAllWick(long chatId) {
        ArrayNode wicks = this.afterDarkAPI.getAllWick();
        this.iterator = createIterator(wicks);

        List<SendMessage> result = new ArrayList<>();
        result.add(setMessage(chatId, "Отлично теперь выберите фитиль"));
        result.add(nextWickInCustomCandle(chatId));
        return result;
    }

    public SendMessage nextWickInCustomCandle(long chatId) {
        JsonNode wick = next();
        String name = wick.get("name").asText();
        String id = wick.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomWick(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage previousWickInCustomCandle(long chatId) {
        JsonNode wick = previous();
        String name = wick.get("name").asText();
        String id = wick.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomWick(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage setWick(long chatId, long wickId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("wick", wickId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали фитиль!";
        return setMessage(chatId, textToSend);
    }

    public List<SendMessage> getAllColor(long chatId) {
        ArrayNode colors = this.afterDarkAPI.getAllColor();
        this.iterator = createIterator(colors);

        List<SendMessage> result = new ArrayList<>();
        result.add(setMessage(chatId, "Выберите цвет формы"));
        result.add(nextColorInCustomCandle(chatId));

        return result;
    }

    public SendMessage nextColorInCustomCandle(long chatId) {
        JsonNode color = next();
        String name = color.get("name").asText();
        String id = color.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomColor(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage previousColorInCustomCandle(long chatId) {
        JsonNode color = previous();
        String name = color.get("name").asText();
        String id = color.get("id").asText("id");
        String textToSend = id + ". " + name;

        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addKeyboardMarkupForCustomColor(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage setColor(long chatId, long colorId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        candleMap.put("colorShape", colorId);
        this.candleMaps.put(chatId, candleMap);
        String textToSend = "Отлично вы успешно выбрали Цвет!";
        return setMessage(chatId, textToSend);
    }

    public SendMessage setCandle(long chatId) {
        Map<String, Long> candleMap = this.candleMaps.get(chatId);
        this.afterDarkAPI.createNewCandle(chatId, candleMap);
        String textToSend = "Поздравляю, вы создали свою свечку";
        return setMessage(chatId, textToSend);
    }

    public SendMessage startSetOrder(long chatId) {
        if (!this.afterDarkAPI.findUser(chatId)) {
            return setMessage(chatId, "Для Начала выполните команду /start");
        }

        ArrayNode candles = this.afterDarkAPI.getCandlesInBasket(chatId);
        if (candles.isEmpty()) {
            return setMessage(chatId, "Пока что у вас пустая корзина.\nДля начала пополните корзину.");
        }

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

        JsonNode newOrder = this.afterDarkAPI.createNewOrder(order);
        String textToSend = "Поздравляю вы оформили заказ";
        this.afterDarkAPI.deleteCandlesInBusket(chatId);
        if (newOrder == null)
            textToSend = "К сожалению не получилось оформить заказ, попробуйте снова!";

        return setMessage(chatId, textToSend);
    }

    private List<SendMessage> setListCandleInBusket(long chatId, ArrayNode candles) {
        List<SendMessage> result = new ArrayList<>();
        for (JsonNode candle : candles) {
            String textToSend = setCandleMessage(candle);
            String id = candle.get("id").asText();
            InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.deleteInlineKeyboardMarkup(id);

            result.add(setMessage(chatId, textToSend, markupInline));
        }
        // ReplyKeyboardMarkup keyboardMarkup =
        // this.replyKeyboardFactory.basketKeyboardMarkup();

        // String textToSend = "Хотите ли оформить заказ?";
        // result.add(setMessage(chatId, textToSend));
        return result;
    }

    public SendMessage nextCandleInCatalog(long chatId) {
        String id;

        JsonNode candle = next();
        String textToSend = setCandleMessage(candle);
        id = candle.get("id").asText();
        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addInBasketInlineKeyboardMarkup(id);
        return setMessage(chatId, textToSend, markupInline);
    }

    public SendMessage previousCandleInCatalog(long chatId) {
        String id;

        JsonNode candle = previous();
        id = candle.get("id").asText();
        String textToSend = setCandleMessage(candle);
        InlineKeyboardMarkup markupInline = this.inlineKeyboardFactory.addInBasketInlineKeyboardMarkup(id);

        return setMessage(chatId, textToSend, markupInline);

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

    private JsonNode previous() {
        previousButton = true;

        if (this.iterator == null) {
            return null;
        }

        if (this.iterator.hasPrevious()) {
            JsonNode json = this.iterator.previous();

            if (nextButton) {
                nextButton = false;

                if (!this.iterator.hasPrevious()) {
                    while (this.iterator.hasNext()) {
                        this.iterator.next();
                    }
                }

                json = this.iterator.previous();
            }

            return json;
        }

        while (this.iterator.hasNext()) {
            this.iterator.next();
        }

        return previous();
    }

    private JsonNode next() {
        nextButton = true;

        if (this.iterator == null) {
            return null;
        }

        if (this.iterator.hasNext()) {
            JsonNode json = this.iterator.next();

            if (previousButton) {
                previousButton = false;

                if (!this.iterator.hasNext()) {
                    while (this.iterator.hasPrevious()) {
                        this.iterator.previous();
                    }
                }
                json = this.iterator.next();
            }

            return json;
        }

        while (this.iterator.hasPrevious()) {
            this.iterator.previous();
        }

        return next();
    }

    private ListIterator<JsonNode> createIterator(ArrayNode arrayNode) {
        List<JsonNode> nodeList = new ArrayList<>();
        arrayNode.forEach(nodeList::add);
        return nodeList.listIterator();
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
