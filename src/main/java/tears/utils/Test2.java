package tears.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Test2 {
    private static final String ETHER_ADDRESS="https://greenchain.space:4443";

    public static void main(String args[]) throws IOException, ExecutionException, InterruptedException, CipherException {

        Admin admin = Admin.build(new HttpService(ETHER_ADDRESS));  // defaults to http://localhost:8545/

        PersonalUnlockAccount personalUnlockAccount = admin.personalUnlockAccount("a9c998d1215d83ad4db4de1541936aba5b8e01b7", "123",BigInteger.valueOf(1000)).
                send();

        Web3j web3 = Web3j.build(new HttpService(ETHER_ADDRESS));  // defaults to http://localhost:8545/
        File file = new File("/Users/maksimtikhonov/Desktop/keyseth/UTC_2018_10_24T09_14_44_350698948Z.json");

        Credentials credentials = WalletUtils.loadCredentials("123",file);

        System.out.println(personalUnlockAccount.accountUnlocked());
        System.out.println();

        List<Type> inputParameters = new ArrayList<>();

        Type exp = new Uint(BigInteger.valueOf(1));
        Type a = new Uint(BigInteger.valueOf(1));
        inputParameters.add(exp);
        inputParameters.add(a);

        Function function = new Function(
                "putDeposit",inputParameters, Collections.emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        Transaction transaction = Transaction.createFunctionCallTransaction("0xa9c998d1215d83ad4db4de1541936aba5b8e01b7",
                BigInteger.valueOf(22), BigInteger.valueOf(410000000),BigInteger.valueOf(90000),
                "0x91b51e8346843fd1f3a49802f4c53e6c52cfb068",BigInteger.valueOf(1800000000),encodedFunction);

        String encodedConstructor =
                FunctionEncoder.encodeConstructor(
                        Arrays.asList(
                                new Uint(BigInteger.valueOf(1)),
                                new Uint(BigInteger.valueOf(1))));

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                BigInteger.valueOf(23), BigInteger.valueOf(410000000),BigInteger.valueOf(90000),
                "0xbaf2739e5125e8f9b9d6b0fb4f9ee3ec9d036d76",encodedConstructor);

        byte[] bytes = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(bytes);

        System.out.println(hexValue);

        EthSendTransaction transactionResponse =web3.ethSendRawTransaction(hexValue)
                .sendAsync().get();

        //String hexValue = Numeric.toHexString(signedMessage);

        System.out.println("value is "+transactionResponse.getTransactionHash());

//        org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse =
//                admin.ethSendTransaction(transaction).send();

        String transactionHash = transactionResponse.getResult();
        System.out.println(transactionHash);

        String str = "{\"ok\":true,\"result\":{\"file_id\":\"BQADAgADVwIAAkJH0EqSUnZOcKEKOwI\",\"file_size\":491,\"file_path\":\"documents/file_3\"}}";
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(str).getAsJsonObject();
        //System.out.println(o.getAsJsonObject("result").get("file_id"));
    }
}
