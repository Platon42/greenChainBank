package tears.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MainBot {

    public static void main(String[] args) {

            ApiContextInitializer.init();
            TelegramBotsApi botsApi = new TelegramBotsApi();
            try {
                botsApi.registerBot(new TGBot());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
}
