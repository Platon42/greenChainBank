package tears.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.UUID;

public class EtherUtils {

    private static final String ETHER_ADDRESS="https://greenchain.space:4443";

    public static boolean isAuth (String address, String password) throws IOException{

        Admin admin = Admin.build(new HttpService(ETHER_ADDRESS));  // defaults to http://localhost:8545/
        PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password).send();
        System.out.println("passw"+password);
        return personalUnlockAccount.accountUnlocked();
    }

    public static boolean putDeposit(
            String address, String password, int amount, File credts) throws IOException, CipherException {

        Web3j web3 = Web3j.build(new HttpService(ETHER_ADDRESS));

        Admin admin = Admin.build(new HttpService(ETHER_ADDRESS));  // defaults to http://localhost:8545/
        Credentials credentials = WalletUtils.loadCredentials(password, credts);

        PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount(address, password).send();
        if (personalUnlockAccount.accountUnlocked()) {
            try {

                TransactionReceipt transactionReceipt = Transfer.sendFunds(
                        admin, credentials, address,
                        BigDecimal.valueOf(1.0), Convert.Unit.ETHER)
                        .send();
                //System.out.println(transactionReceipt.getTransactionHash());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return true;
    }
    public static HashMap<String,String> newAccount(Update update) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CipherException, IOException {

        Admin admin = Admin.build(new HttpService(ETHER_ADDRESS));  // defaults to http://localhost:8545/
        JsonParser parser = new JsonParser();

        String seed = UUID.randomUUID().toString().substring(0,5);
        File dir = new File("/Users/maksimtikhonov/Desktop/ethkeys/");

        String walletName = WalletUtils.generateNewWalletFile(seed,dir);
        String body = new String(Files.readAllBytes(Paths.get(dir+"/"+walletName)), StandardCharsets.UTF_8);

        JsonObject o = parser.parse(body).getAsJsonObject();
        String address = "0x"+o.get("address").getAsString();

        HashMap<String,String> res = new HashMap<>();

        res.put("address",address);
        res.put("file",dir+"/"+walletName);
        res.put("pass",seed);

        return res;
    }


}
