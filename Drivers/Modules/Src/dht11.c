#include "stm32f4xx_hal.h"

#define DHT_PORT GPIOB
#define DHT_PIN  GPIO_PIN_9

extern UART_HandleTypeDef huart2;
char tempBuffer[4] = {0,0,0,0};
char humBuffer[4] = {0,0,0,0};

void DWT_Delay_Init(void)
{
    CoreDebug->DEMCR |= CoreDebug_DEMCR_TRCENA_Msk;
    DWT->CYCCNT = 0;
    DWT->CTRL |= DWT_CTRL_CYCCNTENA_Msk;
}

void delay_us(uint32_t us)
{
    uint32_t start = DWT->CYCCNT;
    uint32_t ticks = us * (SystemCoreClock / 1000000);
    while ((DWT->CYCCNT - start) < ticks);
}

void DHT_SetOutput(void)
{
    GPIO_InitTypeDef GPIO_InitStruct = {0};
    GPIO_InitStruct.Pin = DHT_PIN;
    GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
    GPIO_InitStruct.Pull = GPIO_NOPULL;
    GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_LOW;
    HAL_GPIO_Init(DHT_PORT, &GPIO_InitStruct);
}

void DHT_SetInput(void)
{
    GPIO_InitTypeDef GPIO_InitStruct = {0};
    GPIO_InitStruct.Pin = DHT_PIN;
    GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
    GPIO_InitStruct.Pull = GPIO_PULLUP;
    HAL_GPIO_Init(DHT_PORT, &GPIO_InitStruct);
}

uint8_t DHT11_Read(float *temperature, float *humidity)
{
    uint8_t data[5] = {0};
    uint8_t i, j;

    /* START signal */
    DHT_SetOutput();
    HAL_GPIO_WritePin(DHT_PORT, DHT_PIN, GPIO_PIN_RESET);
    HAL_Delay(18);
    HAL_GPIO_WritePin(DHT_PORT, DHT_PIN, GPIO_PIN_SET);
    delay_us(30);
    DHT_SetInput();

    /* DHT response */
    if (HAL_GPIO_ReadPin(DHT_PORT, DHT_PIN) == GPIO_PIN_SET){
        //HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_13);
        return 0;}
    delay_us(80);

    if (HAL_GPIO_ReadPin(DHT_PORT, DHT_PIN) == GPIO_PIN_RESET){
        //HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_14);
        return 0;}
    delay_us(80);

    /* Read 40 bits */
    for (i = 0; i < 5; i++)
    {
        for (j = 0; j < 8; j++)
        {
            /* wait for LOW */
            while (HAL_GPIO_ReadPin(DHT_PORT, DHT_PIN) == GPIO_PIN_RESET);

            /* after 40us check HIGH or LOW */
            delay_us(40);

            if (HAL_GPIO_ReadPin(DHT_PORT, DHT_PIN) == GPIO_PIN_SET)
            {
                data[i] |= (1 << (7 - j));
                while (HAL_GPIO_ReadPin(DHT_PORT, DHT_PIN) == GPIO_PIN_SET);
            }
        }
    }

    /* Checksum */
    if (data[4] != (data[0] + data[1] + data[2] + data[3])){
        //HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_15);
        return 0;}

    *humidity    = data[0];
    *temperature = data[2];

    return 1;
}

void uint8_to_str(uint8_t value, char *buf)
{
    if (value >= 100)
        value = 99;

    buf[0] = (value / 10) + '0';
    buf[1] = (value % 10) + '0';
    buf[2] = '\0';
}

void UART_SendString(char *str)
{
    uint16_t len = 0;
    while (str[len] != '\0') len++;

    HAL_UART_Transmit(&huart2, (uint8_t*)str, len, HAL_MAX_DELAY);
}

void Send_DHT11_UART(float *temperature, float *humidity)
{
	uint8_t t = (uint8_t)(*temperature);
	uint8_t h = (uint8_t)(*humidity);

    uint8_to_str(t, tempBuffer);
    uint8_to_str(h, humBuffer);

    UART_SendString("T:");
    UART_SendString(tempBuffer);
    UART_SendString(" H:");
    UART_SendString(humBuffer);
    UART_SendString("\r\n");
}
