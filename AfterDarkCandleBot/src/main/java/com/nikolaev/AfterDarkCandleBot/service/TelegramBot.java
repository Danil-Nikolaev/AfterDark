package com.nikolaev.AfterDarkCandleBot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.nikolaev.AfterDarkCandleBot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String startCommandMessage = "/start";
    private final String catalogMessage = "Каталог";
    private final String myBasketMessage = "Ваша корзина";
    private final String mainPageMessage = "На главную страницу";
    private final String makeCustomCandleMessage = "Сделать кастомную свечу";
    private final String createNewOrderMessage = "Оформить заказ";

    private final BotConfig botConfig;
    private Message message;
    private Map<Long, BotState> botStateMap;

    public TelegramBot(BotConfig botConfig, @Autowired Message message) {
        this.botConfig = botConfig;
        this.message = message;
        this.botStateMap = new HashMap<>();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            BotState botState = this.botStateMap.get(chatId);

            if (botState != null) {
                switch (botState) {
                    case WAITING_SHAPE:
                        setShape(chatId, Long.valueOf(messageText));
                        getAllSmell(chatId);
                        break;
                    case WAITING_SMELL:
                        setSmell(chatId, Long.valueOf(messageText));
                        getAllColor(chatId);
                        break;
                    case WAITING_COLOR:
                        setColor(chatId, Long.valueOf(messageText));
                        getAllWick(chatId);
                        break;
                    case WAITING_WICK:
                        setWick(chatId, Long.valueOf(messageText));
                        setCandle(chatId);
                        break;

                    case WAITING_NAME:
                        setName(chatId, messageText);
                        getPhone(chatId);
                        break;

                    case WAITING_PHONE:
                        setPhone(chatId, messageText);
                        getAddress(chatId);
                        break;

                    case WAITING_ADDRESS:
                        setAddress(chatId, messageText);
                        createNewOrder(chatId);

                    default:
                        break;
                }
            } else {
                switch(messageText) {
                    case startCommandMessage -> startMessage(chatId, update.getMessage().getChat().getFirstName());
                    case catalogMessage -> catalogMessage(chatId);
                    case myBasketMessage -> candlesInBasket(chatId);
                    case mainPageMessage -> startMessage(chatId, update.getMessage().getChat().getFirstName());
                    case makeCustomCandleMessage -> makeCustomCandle(chatId);
                    case createNewOrderMessage -> startSetOrder(chatId);

                    default -> defaultMessage(chatId, "not");
                }
            }


        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            long chatId = callbackQuery.getMessage().getChatId();

            String data = callbackQuery.getData();
            String[] parts = data.split(":");
            String button = parts[0];

            switch (button) {
                case "addCandleInBasket" -> addInBasket(chatId, parts);
                case "deleteCandleFromBasket" -> deleteCandleFromBasket(chatId, parts);
            }
        }
    }
    
    private void createNewOrder(Long chatId) {
        this.botStateMap.put(chatId, null);
        SendMessage message = this.message.createNewOrder(chatId);
        sendMessage(message);
    }

    private void setAddress(long chatId, String address) {
        SendMessage message = this.message.setAddress(chatId, address);
        sendMessage(message);
    }

    private void getAddress(long chatId) {
        SendMessage message = this.message.getAddress(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_ADDRESS);
        sendMessage(message);
    }

    private void setPhone(long chatId, String phone) {
        SendMessage message = this.message.setPhone(chatId, phone);
        sendMessage(message);
    }

    private void getPhone(long chatId) {
        SendMessage message = this.message.getPhone(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_PHONE);
        sendMessage(message);
    }

    private void setName(long chatId, String name) {
        SendMessage message = this.message.setName(chatId, name);
        sendMessage(message);
    }

    private void startSetOrder(long chatId) {
        SendMessage message = this.message.startSetOrder(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_NAME);
        sendMessage(message);
    }


    private void setCandle(long chatId) {
        this.botStateMap.put(chatId, null);
        SendMessage message = this.message.setCandle(chatId);
        sendMessage(message);
    }

    private void setWick(long chatId, long wickId) {
        SendMessage message = this.message.setWick(chatId, wickId);
        sendMessage(message);
    }

    private void setColor(long chatId, long colorId) {
        SendMessage message = this.message.setColor(chatId, colorId);
        sendMessage(message);
    }

    private void getAllWick(long chatId) {
        List<SendMessage> wicks = this.message.getAllWick(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_WICK);
        for (SendMessage wick : wicks) sendMessage(wick);
    }

    private void setSmell(long chatId, long smellId) {
        SendMessage message = this.message.setSmell(chatId, smellId);
        sendMessage(message);
    }

    private void getAllColor(long chatId) {
        List<SendMessage> colors = this.message.getAllColor(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_COLOR);
        for (SendMessage color : colors) sendMessage(color);
    }

    private void setShape(long chatId, long shapeId) {
        SendMessage message = this.message.setShape(chatId, shapeId);
        sendMessage(message);
    }

    private void getAllSmell(long chatId) {
        List<SendMessage> smells = this.message.getAllSmell(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_SMELL);
        for (SendMessage smell : smells) sendMessage(smell);
    }

    private void makeCustomCandle(long chatId) {
        List<SendMessage> shapes = this.message.makeCustomCandle(chatId);
        this.botStateMap.put(chatId, BotState.WAITING_SHAPE);
        for (SendMessage shape : shapes) sendMessage(shape);
    }

    private void deleteCandleFromBasket(long chatId, String[] parts) {
        String candleId = parts[1];
        sendMessage(this.message.deleteCandleFromMessage(chatId, candleId));
    }

    private void candlesInBasket(long chatId) {
        List<SendMessage> candles = this.message.listCandlesInBasket(chatId);
        for(SendMessage candle : candles) sendMessage(candle);
    }

    private void addInBasket(long chatId, String[] parts) {
        String candleId = parts[1];
        sendMessage(this.message.addInBasketMessage(chatId, candleId));
    }

    private void catalogMessage(long chatId) {
        List<SendMessage> candles = this.message.catalogMessage(chatId);
        for (SendMessage sendMessage : candles) sendMessage(sendMessage); 
    }

    private void startMessage(long chatId, String name) {
        this.botStateMap.put(chatId, null);
        SendMessage message = this.message.startMessage(chatId, name);
        sendMessage(message);
    }

    private void defaultMessage(long chatId, String textToSend) {
        SendMessage message = this.message.defualtMessage(chatId, textToSend);
        sendMessage(message);
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
    
}
