package com.springexample.irrigationagriculture.service.otherServices;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    public void sendMessage(String message) {
        try {
            String chatId = "1447318345";
            String apiToken = "8077235581:AAFjUxB78ShB50_uZtfCrjgbyoCuWB_OfRA";
            String url = String.format(
                    "https://api.telegram.org/bot8077235581:AAFjUxB78ShB50_uZtfCrjgbyoCuWB_OfRA/sendMessage?chat_id=1447318345&text=a",
                    apiToken, chatId);

            new RestTemplate().getForObject(url, String.class);
        } catch (Exception e) {
            System.out.println(e);
           // e.printStackTrace();
        }
    }

}
