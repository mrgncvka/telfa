package base;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Application {
    private static Properties properties;

    public static void main(String[] args) throws IOException {
        init();
        TelegramBot bot = new TelegramBot(properties.getProperty("TOKEN"));
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                System.out.println(update.message().chat().id());
                long chatId = update.message().chat().id();
                SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

    }

    public static void init() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "app.properties";
        properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
    }

}
