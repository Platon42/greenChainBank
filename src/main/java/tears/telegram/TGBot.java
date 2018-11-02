package tears.telegram;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.web3j.crypto.CipherException;
import tears.utils.EtherUtils;
import tears.utils.HibernateUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TGBot extends TelegramLongPollingBot {

    private final static String API = "685005950:AAGug5x_O0IvdXU4aOoV9eLUV-qNXWYTuxM";
    private final static String NAME = "greenchain_bot";

    public void onUpdateReceived(Update update) {

/*
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
*/
    }

    public void onUpdatesReceived(List<Update> updates) {
        SendMessage message = new SendMessage();
        SendDocument document = new SendDocument();

        TGUser tgUser = new TGUser();

        for (Update update : updates) {
            try {

                if (update.hasMessage()) {
                    String address = null;

                    if (update.getMessage().hasText()) {

                        if (update.getMessage().getText().contains("/login")) {

                            message.setChatId(update.getMessage().getChatId())
                                    .setText("Добрый день! Введите свой адрес кошелька");
                            HibernateUtils.setTelegramUser(tgUser, update, 1,false);
                            execute(message);
                        } else {
                            if (update.getMessage().getText().startsWith("0x") && HibernateUtils.getTGStep(update)==1) {

                                HibernateUtils.setTelegramUser(tgUser, update, 2,false); //кошелек
                                message.setChatId(update.getMessage().getChatId())
                                        .setText("Введите пароль");
                                execute(message);

                            } else {
                                if (HibernateUtils.getTGStep(update) == 2 && !update.getMessage().getText().startsWith("0x")) {
                                    HibernateUtils.setTelegramUser(tgUser, update, 3,false);

                                } else {
                                    if (HibernateUtils.getTGStep(update) == 3) {
                                        HibernateUtils.setTelegramUser(tgUser,update, 4,
                                        EtherUtils.isAuth(
                                                HibernateUtils.getTGAddress(update),
                                                HibernateUtils.getTGText(update,3)));

                                    }
                                }
                            }
                        }

                        if (update.getMessage().getText().contains("/new")) {

                            HashMap<String, String> newAccount = EtherUtils.newAccount(update);
                            address = newAccount.get("address");
                            File fWallet = new File(newAccount.get("file"));
                            String pass = newAccount.get("pass");

                            message.setChatId(update.getMessage().getChatId()).setText("Ваш адрес" + "\n"+address);
                            execute(message);
                            message.setChatId(update.getMessage().getChatId()).setText("Ваш пароль" + "\n"+pass);
                            execute(message);
                            message.setChatId(update.getMessage().getChatId()).setText("Ваш файл" + "\n");
                            execute(message);
                            document.setChatId(update.getMessage().getChatId())
                                    .setDocument(fWallet);
                            execute(document);
                            HibernateUtils.setTelegramUser(tgUser, update, 21,false);

                        }
                        if (update.getMessage().getText().contains("/deposit")){

                            List<String> validWallets = HibernateUtils.getValidWallets(update);
                            if (!validWallets.isEmpty()){
                                for (String s : validWallets) {
                                    message.setChatId(update.getMessage().getChatId())
                                            .setText("Вы авторизованы в этих кошельках" + s + "\n");
                                    execute(message);
                                    message.setChatId(update.getMessage().getChatId())
                                            .setText("Выберите один из них" + s + "\n");
                                    execute(message);
                                }
                                HibernateUtils.setTelegramUser(tgUser, update, 31,true);

                            }
                            message.setChatId(update.getMessage().getChatId())
                                    .setText("Добрый день! Введите свой адрес кошелька");

                            HibernateUtils.setTelegramUser(tgUser, update, 32,false);
                            execute(message);
                        }

                        if (update.getMessage().getText().contains("/balance")){
                            message.setChatId(update.getMessage().getChatId())
                                    .setText("Добрый день! Введите свой адрес кошелька");
                            HibernateUtils.setTelegramUser(tgUser, update, 41,false);
                            execute(message);
                        } else {
                                if (update.getMessage().getText().startsWith("0x") && HibernateUtils.getTGStep(update) == 41 ){
                                message.setChatId(update.getMessage().getChatId())
                                        .setText("Ваш баланс " + EtherUtils.getBalance(update.getMessage().getText()));
                                execute(message);
                            }

                        }
                        if (update.getMessage().hasDocument()) {

                            String fileId = update.getMessage().getDocument().getFileId();

                            URL website = new URL("https://api.telegram.org/bot685005950:AAGug5x_O0IvdXU4aOoV9eLUV-qNXWYTuxM/getFile?file_id=" + fileId);
                            InputStream in1 = website.openStream();
                            String TGAnswer = IOUtils.toString(in1, "UTF-8");

                            JsonParser parser = new JsonParser();
                            JsonObject o = parser.parse(TGAnswer).getAsJsonObject();

                            String filePath = o.getAsJsonObject("result").get("file_path").getAsString();

                            URL website2 = new URL("https://api.telegram.org/file/bot685005950:AAGug5x_O0IvdXU4aOoV9eLUV-qNXWYTuxM/" + filePath);
                            InputStream in2 = website2.openStream();
                            String creds = IOUtils.toString(in2, "UTF-8");

                            File file = new File("creds.json");
                            FileUtils.writeStringToFile(file, creds, StandardCharsets.UTF_8);

                            EtherUtils.putDeposit(address, "123", 100, file);
                        }

                    }
                }
            } catch (TelegramApiException | CipherException | IOException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public String getBotUsername() {
        return NAME;
    }

    public String getBotToken() {
        return API;
    }

}
