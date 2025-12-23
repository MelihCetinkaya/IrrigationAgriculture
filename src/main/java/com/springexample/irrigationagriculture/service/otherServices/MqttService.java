package com.springexample.irrigationagriculture.service.otherServices;

import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.Amounts;
import com.springexample.irrigationagriculture.repository.AdminRepo;
import com.springexample.irrigationagriculture.repository.AmountsRepo;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Service
public class MqttService implements Runnable{

    private final AdminRepo adminRepo;
    private MqttClient client;
    private final AtomicBoolean connected = new AtomicBoolean(false);

    @Value("${mqtt.broker}")
    private String brokerUrl;

    @Value("${mqtt.topic.temperature}")
    private String temperature;

    @Value("${mqtt.topic.humidity}")
    private String humidity;

    private Admin admin;
    private final AmountsRepo  amountsRepo;

    public MqttService(AdminRepo adminRepo, AmountsRepo amountsRepo) {
        this.adminRepo = adminRepo;
        this.amountsRepo = amountsRepo;
    }

    public boolean isConnected() {
        return connected.get();
    }

    private void handleTemperature(double value) {
        //System.out.println("🌡 Sıcaklık: " + value);
    }

    private void handleHumidity(double value) {
        //System.out.println("💧 Nem: " + value);
    }


    Map<String, Consumer<Double>> topicHandlers = new HashMap<>();

    private void connectMqtt() {
        try {
            client = new MqttClient(brokerUrl, MqttClient.generateClientId());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);
            connected.set(true);

            topicHandlers.put(temperature, this::handleTemperature);
            topicHandlers.put(humidity, this::handleHumidity);

            IMqttMessageListener listener = (topic, message) -> {
                String payload = new String(message.getPayload());
                double value = 0.0;
                admin=adminRepo.findByUsername("admin1").orElseThrow();
                Amounts amounts;
                try {
                    if(payload.startsWith("temperature:")){
                        String numberPart = payload.substring("temperature:".length());
                        value = Double.parseDouble(numberPart);
                        admin.getPlantHouse().getAmounts().setTempValue(value);

                    }

                   else if(payload.startsWith("humidity:")){
                        String numberPart = payload.substring("humidity:".length());
                        value = Double.parseDouble(numberPart);
                        admin.getPlantHouse().getAmounts().setHumValue(value);
                    }

                    amounts = admin.getPlantHouse().getAmounts();
                    amountsRepo.save(amounts);
                    Consumer<Double> handler = topicHandlers.get(topic);

                    if (handler != null) {
                        handler.accept(value);
                    }
                } catch (Exception e) {
                    System.err.println("🚨 Hatalı mesaj: " + payload);
                }
            };

            client.subscribe(temperature, listener);
            client.subscribe(humidity, listener);

            System.out.println("MQTT bağlantısı başarılı!");
        } catch (Exception e) {
            connected.set(false);
            System.out.println("mqtt bağlantısı başarısız");
            e.printStackTrace();
        }
    }


    @Override
    public void run() {

        connectMqtt();
    }
}
