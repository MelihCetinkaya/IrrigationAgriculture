package com.springexample.irrigationagriculture.service.otherServices;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TelegramService {

    public void sendMessage(String message) {
        try {
            String chatId = "1447318345";
            String apiToken = "8077235581:AAFjUxB78ShB50_uZtfCrjgbyoCuWB_OfRA";

            String encodedMessage =
                    URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url =
                    "https://api.telegram.org/bot" + apiToken +
                            "/sendMessage?chat_id=" + chatId +
                            "&text=" + encodedMessage;

            new RestTemplate().getForObject(url, String.class);
        } catch (Exception e) {
            System.out.println(e);
           // e.printStackTrace();
        }
    }

}
