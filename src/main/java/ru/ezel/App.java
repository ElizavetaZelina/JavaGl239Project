package ru.ezel;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.util.List;


public class App {
    private static final String TOKEN = "5362810157:AAEn8neFhxjnmkjbEqPyXBum_PJCIRxcc6A";
    private static final String BOT_URL = "t.me/mathcasesbot";
    private static final TelegramBot bot = new TelegramBot(TOKEN);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");
        Integer updateId = 0;
        while (true) {
            System.out.print(".");
            GetUpdates getUpdates = new GetUpdates().limit(100).offset(updateId).timeout(0);
            GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
            if (updatesResponse != null) {
                List<Update> updates = updatesResponse.updates();
                if (updates != null) {
                    for (Update update : updates) {
                        updateId = update.updateId() + 1;
                        process(update);
                    }
                }
            }
            Thread.sleep(3000);
        }
    }

    private static void process(Update update) {
        Long chatId = update.message().chat().id();
        String response;
        if (update.message().text().equals("new")) {
            response = "1+1\n2*3";
        } else {
            response = "Use \"new\" command to get cases";
        }


        SendMessage request = new SendMessage(chatId, response)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(1)
                .replyMarkup(new ForceReply());
        bot.execute(request);
    }
}
