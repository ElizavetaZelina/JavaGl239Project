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
import java.util.Random;


public class App {
    private static final String TOKEN = "5362810157:AAEn8neFhxjnmkjbEqPyXBum_PJCIRxcc6A";
    private static final String BOT_URL = "t.me/mathcasesbot";
    private static final TelegramBot bot = new TelegramBot(TOKEN);

    public static void main(String[] args) throws InterruptedException {
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
        if (update.message() == null) {
            System.out.print(" null msg ");
            return;
        }
        if (update.message().chat() == null) {
            System.out.print(" null chat ");
            return;
        }
        Long chatId = update.message().chat().id();
        String response;
        String text = update.message().text();
        if (text.startsWith("new")) {
            response = getCases(text);
        } else {
            response = "Use \"new\" command to get cases";
        }


        SendMessage request = new SendMessage(chatId, response)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true)
                .replyToMessageId(update.message().messageId())
                .replyMarkup(new ForceReply());
        bot.execute(request);
    }

    private static String getCases(String text) {
        String[] data = text.split(" ");
        if (data.length != 4) {
            return "Usage: new maxnum minnum casesnum";
        }
        try {
            int maxnum = Integer.parseInt(data[1]);
            int minnum = Integer.parseInt(data[2]);
            int casesnum = Integer.parseInt(data[3]);
            return generateCases(maxnum, minnum, casesnum);
        } catch (Exception e) {
            return "Invalid parameters " + data[1] + " " + data[2] + " " + data[3] + " Usage: new maxnum minnum casesnum";
        }
    }

    private static String generateCases(int maxnum, int minnum, int casesnum) {
        if (maxnum < minnum) {
            return "Invalid min-max nums";
        }
        if (casesnum <= 0 || casesnum > 100) {
            return "Invalid cases num. 0 < x < 100";
        }
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < casesnum; i++) {
            int sign = r.nextInt(4);
            String sSign;
            switch (sign) {
                case 0:
                    sSign = "+";
                    break;
                case 1:
                    sSign = "-";
                    break;
                case 2:
                    sSign = "*";
                    break;
                case 3:
                    sSign = ":";
                    break;
                default:
                    sSign = "+";
            }
            int first = r.nextInt(maxnum - minnum);
            first = first + minnum;
            int second = r.nextInt(maxnum - minnum);
            second = second + minnum;
            sb.append(first + " " + sSign + " " + second + " =\n");
        }
        return sb.toString();
    }
}
