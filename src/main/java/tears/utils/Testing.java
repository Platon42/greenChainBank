package tears.utils;

import com.google.gson.*;
import tears.request.Client;
import tears.request.Credit;
import tears.request.CreditParam;
import tears.response.PDResponse;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

//import tears.communication.SMSSender;

public class Testing {

    public static void main (String arg[]) throws Exception {


        List<String> strings = Files.readAllLines(Paths.get(
                "/Users/maksimtikhonov/IdeaProjects/greenChainBank/src/main/resources/request.json"));

        StringBuilder body = new StringBuilder();
        for (String s : strings) {
            body.append(s).append("\n");

        }

        JsonDeserializer dateTimeDsr =
                (jsonElement, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString());

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,dateTimeDsr).excludeFieldsWithoutExposeAnnotation().create();
        final Client client = gson.fromJson(body.toString(), Client.class);

      // SMSSender.sendSMS("1","2");
        //HibernateUtils.setAllBankFields(client);
        System.out.println(PDClient.calcPD(client));

        Clock clock = Clock.system(ZoneId.of("Europe/Moscow"));
        LocalDateTime ldt2 = LocalDateTime.now(clock);

        PDResponse pdResponse = new PDResponse();
        pdResponse.setPd(1);
        pdResponse.setDecision(true);
        String a = gson.toJson(pdResponse);
        System.out.println(a);

        String str = "{\"ok\":true,\"result\":{\"file_id\":\"BQADAgADVwIAAkJH0EqSUnZOcKEKOwI\",\"file_size\":491,\"file_path\":\"documents/file_3\"}}";
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(str).getAsJsonObject();

        String filePath =o.get("file_path").getAsString();
        System.out.println(filePath);

        //HibernateUtils.setRiskMetrics(pdResponse,client,1);

        Set<Credit> credits = client.getProductInfo().getCredits();
        for (Credit c : credits) {
            //System.out.println(client.getClientInfo().getEducation());
            Set<CreditParam> creditParams = c.getCreditParams();
            for (CreditParam creditParam : creditParams) {

            }

        }
    }
}
