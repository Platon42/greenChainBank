package tears.communication;

import ru.dezhik.sms.sender.SenderService;
import ru.dezhik.sms.sender.SenderServiceConfiguration;
import ru.dezhik.sms.sender.SenderServiceConfigurationBuilder;
import ru.dezhik.sms.sender.api.InvocationStatus;
import ru.dezhik.sms.sender.api.smsru.SMSRuResponseStatus;
import ru.dezhik.sms.sender.api.smsru.cost.SMSRuCostRequest;
import ru.dezhik.sms.sender.api.smsru.cost.SMSRuCostResponse;

import java.io.IOException;

public class SMSSender {

    public static boolean sendSMS(String number, String text) throws IOException {

        SenderServiceConfiguration config = SenderServiceConfigurationBuilder.create()
                .setApiId("3575745D-AF9C-86C2-C03C-CBC60E80AABA")
                .setTestSendingEnabled(true)
                .setReturnPlainResponse(true)
                .build();

        SenderService sender = new SenderService(config);

        SMSRuCostRequest sendRequest = new SMSRuCostRequest();
        sendRequest.setReceiver("+79168600070");
        sendRequest.setText("Hello world");
        SMSRuCostResponse getCostResponse = sender.execute(sendRequest);

        boolean success;

        if (sendRequest.getStatus() == InvocationStatus.SUCCESS) {
            /**
             * request was executed successfully now you can handle sendResponse
             * always check service API manual page for determine which codes should be treated as successful
             */
            if (getCostResponse.getResponseStatus() == SMSRuResponseStatus.IN_QUEUE) {
                System.out.println(String.format("Success. Price %4.2f, smsNeeded %d", getCostResponse.getPrice(), getCostResponse.getSmsNeeded()));
            } else {
                System.out.println(String.format("Failed with status %s", getCostResponse.getResponseStatus()));
            }

            success = true;

        } else {
            /**
             * Something went wrong and request failed, check sendRequest.getStatus()
             * usually the problem is in params validation or network or parsing response from the remote API.
             * {@link ru.dezhik.sms.sender.api.InvocationStatus}
             */
            if (sendRequest.getStatus().isAbnormal()) {
                sendRequest.getException().printStackTrace();
            }
            success = false;

        }
        sender.shutdown();
        return success;
    }
}
