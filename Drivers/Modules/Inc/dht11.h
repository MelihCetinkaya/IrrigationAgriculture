#ifndef MODULES_INC_DHT11_H_
#define MODULES_INC_DHT11_H_

void DWT_Delay_Init(void);
void delay_us(uint32_t us);
void DHT_SetOutput(void);
void DHT_SetInput(void);
uint8_t DHT11_Read(float *temperature, float *humidity);
void uint8_to_str(uint8_t value, char *buf);
void UART_SendString(char *str);
void Send_DHT11_UART(float *temperature, float *humidity);

#endif
