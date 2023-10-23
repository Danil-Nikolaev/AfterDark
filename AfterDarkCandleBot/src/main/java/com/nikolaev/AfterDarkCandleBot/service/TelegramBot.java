package com.nikolaev.AfterDarkCandleBot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.nikolaev.AfterDarkCandleBot.config.BotConfig;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String startCommandMessage = "/start";
    private final String basketCommandMessage = "/basket";
    private final String customCommandMessage = "/custom";
    private final String orderCommandMessage = "/order";
    private final String ordersCommandMessage = "/orders";
    private final String catalogCommandMessage = "/catalog";
    private final String helpCommandMessage = "/help";

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
                switch (messageText) {
                    case startCommandMessage -> startMessage(chatId, update.getMessage().getChat().getFirstName());
                    case catalogCommandMessage -> catalogMessage(chatId);
                    case basketCommandMessage -> candlesInBasket(chatId);
                    case customCommandMessage -> makeCustomCandle(chatId);
                    case orderCommandMessage -> startSetOrder(chatId);
                    case helpCommandMessage -> helpCommand(chatId);
                    case ordersCommandMessage -> ordersCommand(chatId);
                    default -> defaultMessage(chatId, "Выберите одну из команд.");
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
                case "nextCandleInCatalog" -> nextCandleInCatalog(chatId, callbackQuery.getMessage().getMessageId());
                case "previousCandleInCatalog" ->
                    previousCandleInCatalog(chatId, callbackQuery.getMessage().getMessageId());
                case "nextShapeInCustomCandle" ->
                    nextShapeInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "previousShapeInCustomCandle" ->
                    previousShapeInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "addShapeInCandle" -> addShapeInCandle(chatId, parts);
                case "nextSmellInCustomCandle" ->
                    nextSmellInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "previousSmellInCustomCandle" ->
                    previousSmellInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "addSmellInCandle" -> addSmellInCandle(chatId, parts);
                case "nextWickInCustomCandle" ->
                    nextWickInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "previousWickInCustomCandle" ->
                    previousWickInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "addWickInCandle" -> addWickInCandle(chatId, parts);
                case "nextColorInCustomCandle" ->
                    nextColorInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "previousColorInCustomCandle" ->
                    previousColorInCustomCandle(chatId, callbackQuery.getMessage().getMessageId());
                case "addColorInCandle" -> addColorInCandle(chatId, parts);
            }
        }
    }

    public void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();

        commands.add(new BotCommand("start", "Начать"));
        commands.add(new BotCommand("basket", "Ваша корзина"));
        commands.add(new BotCommand("catalog", "Каталог"));
        commands.add(new BotCommand("custom", "Сделать кастомную свечу"));
        commands.add(new BotCommand("order", "Оформить заказ"));
        commands.add(new BotCommand("orders", "Посмотреть свои заказы"));
        commands.add(new BotCommand("help", "Помощь"));

        SetMyCommands setMyCommands = new SetMyCommands();
        setMyCommands.setCommands(commands);

        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void addColorInCandle(long chatId, String[] parts) {
        String data = parts[1];
        setColor(chatId, Long.valueOf(data));
        setCandle(chatId);
    }

    private void previousColorInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.previousColorInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void nextColorInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.nextColorInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void addWickInCandle(long chatId, String[] parts) {
        String data = parts[1];
        setWick(chatId, Long.valueOf(data));
        getAllColor(chatId);
    }

    private void previousWickInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.previousWickInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void nextWickInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.nextWickInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void addSmellInCandle(long chatId, String[] parts) {
        String data = parts[1];
        setSmell(chatId, Long.valueOf(data));
        getAllWick(chatId);
    }

    private void previousSmellInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.previousSmellInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void nextSmellInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.nextSmellInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void addShapeInCandle(long chatId, String[] parts) {
        String data = parts[1];
        setShape(chatId, Long.valueOf(data));
        getAllSmell(chatId);
    }

    private void previousShapeInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.previousShapeInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void nextShapeInCustomCandle(long chatId, int messageId) {
        SendMessage message = this.message.nextShapeInCustomCandle(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void previousCandleInCatalog(long chatId, int messageId) {
        SendMessage message = this.message.previousCandleInCatalog(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    private void nextCandleInCatalog(long chatId, int messageId) {
        SendMessage message = this.message.nextCandleInCatalog(chatId);
        EditMessageText editMessageText = convertSendMessageToEditMessageText(message, messageId);

        sendMessage(editMessageText);
    }

    public static EditMessageText convertSendMessageToEditMessageText(SendMessage sendMessage, int messageId) {
        EditMessageText editMessageText = new EditMessageText();

        editMessageText.setChatId(sendMessage.getChatId());
        editMessageText.setMessageId(messageId);
        editMessageText.setText(sendMessage.getText());

        if (sendMessage.getReplyMarkup() instanceof InlineKeyboardMarkup) {
            editMessageText.setReplyMarkup((InlineKeyboardMarkup) sendMessage.getReplyMarkup());
        }

        return editMessageText;
    }

    private void helpCommand(long chatId) {
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
        SendMessage message = this.message.defualtMessage(chatId, textToSend);
        sendMessage(message);
    }

    private void ordersCommand(long chatId) {
        List<SendMessage> messages = this.message.findAllOrdersByUser(chatId);
        for (SendMessage messageLocal : messages) {
            sendMessage(messageLocal);
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
        String badMessage = "Пока что у вас";
        String dontFindUser = "Для Начала выполните";
        if (!message.getText().startsWith(badMessage) && !message.getText().startsWith(dontFindUser)) {
            this.botStateMap.put(chatId, BotState.WAITING_NAME);
        }

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
        // this.botStateMap.put(chatId, BotState.WAITING_WICK);
        for (SendMessage wick : wicks)
            sendMessage(wick);
    }

    private void setSmell(long chatId, long smellId) {
        SendMessage message = this.message.setSmell(chatId, smellId);
        sendMessage(message);
    }

    private void getAllColor(long chatId) {
        List<SendMessage> colors = this.message.getAllColor(chatId);
        // this.botStateMap.put(chatId, BotState.WAITING_COLOR);
        for (SendMessage color : colors)
            sendMessage(color);
    }

    private void setShape(long chatId, long shapeId) {
        SendMessage message = this.message.setShape(chatId, shapeId);
        sendMessage(message);
    }

    private void getAllSmell(long chatId) {
        List<SendMessage> smells = this.message.getAllSmell(chatId);
        // this.botStateMap.put(chatId, BotState.WAITING_SMELL);
        for (SendMessage smell : smells)
            sendMessage(smell);
    }

    private void makeCustomCandle(long chatId) {
        List<SendMessage> shapes = this.message.getAllShape(chatId);
        // this.botStateMap.put(chatId, BotState.WAITING_SHAPE);
        for (SendMessage shape : shapes)
            sendMessage(shape);
    }

    private void deleteCandleFromBasket(long chatId, String[] parts) {
        String candleId = parts[1];
        sendMessage(this.message.deleteCandleFromMessage(chatId, candleId));
    }

    private void candlesInBasket(long chatId) {
        List<SendMessage> candles = this.message.listCandlesInBasket(chatId);
        for (SendMessage candle : candles)
            sendMessage(candle);
    }

    private void addInBasket(long chatId, String[] parts) {
        String candleId = parts[1];
        sendMessage(this.message.addInBasketMessage(chatId, candleId));
    }

    private void catalogMessage(long chatId) {
        SendMessage candle = this.message.catalogMessage(chatId);
        sendMessage(candle);
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

    private void sendMessage(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
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
