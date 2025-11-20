package com.springexample.irrigationagriculture.configuration;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UartConfig {

    @Bean
    public SerialPort UartPort() {

        SerialPort serialPort = SerialPort.getCommPort("/dev/serial0"); //  /dev/serial0
        serialPort.setComPortParameters(115200, 8, 1, 0);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        try {
            if (serialPort.openPort()) {
                System.out.println("UART port açıldı: " + serialPort.getSystemPortName());
            } else {
                System.out.println("UART port açılamadı: " + serialPort.getSystemPortName());
            }
        } catch (Exception e) {
            System.err.println("UART port açılırken hata oluştu: " + e.getMessage());
        }
        return serialPort;
    }

}
