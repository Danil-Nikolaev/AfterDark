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
import com.nikolaev.AfterDarkCandleBot.messages.BasketMessage;
import com.nikolaev.AfterDarkCandleBot.messages.CatalogMessage;
import com.nikolaev.AfterDarkCandleBot.messages.ColorMessage;
import com.nikolaev.AfterDarkCandleBot.messages.CustomCandleMessage;
import com.nikolaev.AfterDarkCandleBot.messages.HelpCommandMessage;
import com.nikolaev.AfterDarkCandleBot.messages.OrdersMessage;
import com.nikolaev.AfterDarkCandleBot.messages.ShapeMessage;
import com.nikolaev.AfterDarkCandleBot.messages.SmellMessage;
import com.nikolaev.AfterDarkCandleBot.messages.StartCommandMessage;
import com.nikolaev.AfterDarkCandleBot.messages.WickMessage;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.BeingAdded;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Deletable;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.IterableMessage;
import com.nikolaev.AfterDarkCandleBot.messages.interfaces.Message;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String startCommandMessageString = "/start";
    private final String basketCommandMessage = "/basket";
    private final String customCommandMessage = "/custom";
    private final String orderCommandMessage = "/order";
    private final String ordersCommandMessage = "/orders";
    private final String catalogCommandMessage = "/catalog";
    private final String helpCommandMessageString = "/help";

    private final BotConfig botConfig;
    private MessageService message;
    private Map<Long, BotState> botStateMap = new HashMap<>();

    private StartCommandMessage startCommandMessage;
    private HelpCommandMessage helpCommandMessage;
    private OrdersMessage ordersMessage;
    private CatalogMessage catalogMessage;
    private BasketMessage basketMessage;
    private ShapeMessage shapeMessage;
    private SmellMessage smellMessage;
    private ColorMessage colorMessage;
    private WickMessage wickMessage;
    private CustomCandleMessage customCandleMessage;

    @Autowired
    public TelegramBot(BotConfig botConfig, MessageService message,
            StartCommandMessage startCommandMessage, HelpCommandMessage helpCommandMessage,
            OrdersMessage ordersMessage, CatalogMessage catalogMessage, BasketMessage basketMessage,
            ShapeMessage shapeMessage, SmellMessage smellMessage, ColorMessage colorMessage, WickMessage wickMessage, CustomCandleMessage customCandleMessage) {
        this.botConfig = botConfig;
        this.message = message;
        this.startCommandMessage = startCommandMessage;
        this.helpCommandMessage = helpCommandMessage;
        this.ordersMessage = ordersMessage;
        this.catalogMessage = catalogMessage;
        this.basketMessage = basketMessage;
        this.shapeMessage = shapeMessage;
        this.colorMessage = colorMessage;
        this.smellMessage = smellMessage;
        this.wickMessage = wickMessage;
        this.customCandleMessage = customCandleMessage;
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
                    case startCommandMessageString -> sendMessage(startCommandMessage, update);
                    case catalogCommandMessage -> sendMessage(catalogMessage, update);
                    case basketCommandMessage -> sendMessage(basketMessage, update);
                    case customCommandMessage -> sendMessage(shapeMessage, update);
                    case orderCommandMessage -> startSetOrder(chatId);
                    case helpCommandMessageString -> sendMessage(helpCommandMessage, update);
                    case ordersCommandMessage -> sendMessage(ordersMessage, update);
                    default -> defaultMessage(chatId, "Выберите одну из команд.");
                }
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            String data = callbackQuery.getData();
            String[] parts = data.split(":");
            String button = parts[0];

            switch (button) {
                case "addCandleInBasket" -> add(catalogMessage, update);
                case "deleteCandleFromBasket" -> delete(basketMessage, update);
                case "nextCandleInCatalog" -> next(catalogMessage, update);
                case "previousCandleInCatalog" -> previous(catalogMessage, update);
                case "nextShapeInCustomCandle" -> next(shapeMessage, update);
                case "previousShapeInCustomCandle" -> previous(shapeMessage, update);
                case "addShapeInCandle" -> addShape(update);
                case "nextSmellInCustomCandle" -> next(smellMessage, update);
                case "previousSmellInCustomCandle" -> previous(smellMessage, update);
                case "addSmellInCandle" -> addSmell(update);
                case "nextWickInCustomCandle" -> next(wickMessage, update);
                case "previousWickInCustomCandle" -> previous(wickMessage, update);
                case "addWickInCandle" -> addWick(update);
                case "nextColorInCustomCandle" -> next(colorMessage, update);
                case "previousColorInCustomCandle" -> previous(colorMessage, update);
                case "addColorInCandle" -> addColor(update);
            }
        }
    }

    private void sendMessage(Message message, Update update) {
        for (SendMessage mes : message.setMessage(update))
            sendMessage(mes);
    }

    private void next(IterableMessage iterableMessage, Update update) {
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        EditMessageText editText = convertSendMessageToEditMessageText(iterableMessage.next(update), messageId);
        sendMessage(editText);
    }

    private void previous(IterableMessage iterableMessage, Update update) {
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        EditMessageText editText = convertSendMessageToEditMessageText(iterableMessage.previous(update), messageId);
        sendMessage(editText);
    }

    private void add(BeingAdded added, Update update) {
        sendMessage(added.add(update));
    }

    private void delete(Deletable deletable, Update update) {
        sendMessage(deletable.delete(update));
    }

    private void addShape(Update update) {
        add(shapeMessage, update);
        sendMessage(colorMessage, update);
    }

    private void addColor(Update update) {
        add(colorMessage, update);
        sendMessage(wickMessage, update);
    }

    private void addWick(Update update) {
        add(wickMessage, update);
        sendMessage(smellMessage, update);
    }

    private void addSmell(Update update) {
        add(smellMessage, update);
        add(customCandleMessage, update);
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
