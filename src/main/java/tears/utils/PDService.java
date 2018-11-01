package tears.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import tears.request.Client;
import tears.response.PDResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@WebServlet("/CreditDecision")
public class PDService extends HttpServlet {

    private static PDResponse pdResponse = new PDResponse();

    @Override
    public void init() {

    }

    @Override
    protected void doGet(HttpServletRequest httpreq, HttpServletResponse httpresp) {

        try {

            PrintWriter writer = httpresp.getWriter();
            httpreq.setCharacterEncoding("UTF-8");
            writer.print("Hello!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest httpreq, HttpServletResponse httpresp) throws IOException {

        PrintWriter writer = httpresp.getWriter();
        httpreq.setCharacterEncoding("UTF-8");

        boolean decision;
        String body = httpreq.getReader().lines().collect(Collectors.joining());
        JsonDeserializer dateTimeDsr =
                (jsonElement, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString());

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,dateTimeDsr).excludeFieldsWithoutExposeAnnotation().create();
        final Client client = gson.fromJson(body, Client.class);

        try {
            HibernateUtils.setAllBankFields(client);
            int PD = PDClient.calcPD(client).get("PD");

            HibernateUtils.setRiskMetrics(pdResponse,client,PD);
            decision = 15 <= PD && PD <= 56;

            pdResponse.setAddress(client.getAddress());
            pdResponse.setDecision(decision);
            pdResponse.setPd(PD);

            writer.print(gson.toJson(pdResponse));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

