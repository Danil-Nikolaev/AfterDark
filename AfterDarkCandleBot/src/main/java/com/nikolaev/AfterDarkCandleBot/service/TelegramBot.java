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
import com.nikolaev.AfterDarkCandleBot.messages.AddressMessage;
import com.nikolaev.AfterDarkCandleBot.messages.BasketMessage;
import com.nikolaev.AfterDarkCandleBot.messages.CatalogMessage;
import com.nikolaev.AfterDarkCandleBot.messages.ColorMessage;
import com.nikolaev.AfterDarkCandleBot.messages.CustomCandleMessage;
import com.nikolaev.AfterDarkCandleBot.messages.DefaultMessage;
import com.nikolaev.AfterDarkCandleBot.messages.HelpCommandMessage;
import com.nikolaev.AfterDarkCandleBot.messages.NameMessage;
import com.nikolaev.AfterDarkCandleBot.messages.OrderMessage;
import com.nikolaev.AfterDarkCandleBot.messages.OrdersMessage;
import com.nikolaev.AfterDarkCandleBot.messages.PhoneMessage;
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
    private DefaultMessage defaultMessage;
    private NameMessage nameMessage;
    private PhoneMessage phoneMessage;
    private AddressMessage addressMessage;
    private OrderMessage orderMessage;

    @Autowired
    public TelegramBot(BotConfig botConfig,
            StartCommandMessage startCommandMessage, HelpCommandMessage helpCommandMessage,
            OrdersMessage ordersMessage, CatalogMessage catalogMessage, BasketMessage basketMessage,
            ShapeMessage shapeMessage, SmellMessage smellMessage, ColorMessage colorMessage, WickMessage wickMessage, 
            CustomCandleMessage customCandleMessage, DefaultMessage defaultMessage, 
            NameMessage nameMessage, PhoneMessage phoneMessage, AddressMessage addressMessage, OrderMessage orderMessage) {
        this.botConfig = botConfig;
        this.startCommandMessage = startCommandMessage;
        this.helpCommandMessage = helpCommandMessage;
        this.ordersMessage = ordersMessage;
        this.catalogMessage = catalogMessage;
        this.basketMessage = basketMessage;
        this.shapeMessage = shapeMessage;
        this.colorMessage = colorMessage;
        this.smellMessage = smellMessage;
        this.wickMessage = wickMessage;
        this.defaultMessage = defaultMessage;
        this.customCandleMessage = customCandleMessage;
        this.nameMessage = nameMessage;
        this.phoneMessage = phoneMessage;
        this.addressMessage = addressMessage;
        this.orderMessage = orderMessage;
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
                        add(nameMessage, update);
                        sendMessage(phoneMessage, update);
                        this.botStateMap.put(chatId, BotState.WAITING_PHONE);
                        break;

                    case WAITING_PHONE:
                        add(phoneMessage, update);
                        sendMessage(addressMessage, update);
                        this.botStateMap.put(chatId, BotState.WAITING_ADDRESS);
                        break;
                    case WAITING_ADDRESS:
                        add(addressMessage, update);
                        add(orderMessage, update);
                        this.botStateMap.put(chatId, null);

                    default:
                        break;
                }
            } else {
                switch (messageText) {
                    case startCommandMessageString -> sendMessage(startCommandMessage, update);
                    case catalogCommandMessage -> sendMessage(catalogMessage, update);
                    case basketCommandMessage -> sendMessage(basketMessage, update);
                    case customCommandMessage -> sendMessage(shapeMessage, update);
                    case orderCommandMessage -> startSetOrder(update);
                    case helpCommandMessageString -> sendMessage(helpCommandMessage, update);
                    case ordersCommandMessage -> sendMessage(ordersMessage, update);
                    default -> sendMessage(defaultMessage, update);
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

    private void startSetOrder(Update update) {
        long chatId = update.getMessage().getChatId();
        List<SendMessage> messages = this.nameMessage.setMessage(update);
        SendMessage message = messages.get(0);
        String badMessage = "Пока что у вас";
        String dontFindUser = "Для Начала выполните";
        if (!message.getText().startsWith(badMessage) && !message.getText().startsWith(dontFindUser)) {
            this.botStateMap.put(chatId, BotState.WAITING_NAME);
        }

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
