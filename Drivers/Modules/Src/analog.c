#include "analog.h"
#include "dht11.h"

extern ADC_HandleTypeDef hadc1;

char adc1Buffer[6];
char adc2Buffer[6];
char adc3Buffer[6];

void sendAnalogValues(void)
{
    uint16_t adc_value1;
    uint16_t adc_value2;
    uint16_t adc_value3;

    HAL_ADC_Start(&hadc1);

    HAL_ADC_PollForConversion(&hadc1, HAL_MAX_DELAY);
    adc_value1 = (uint16_t)HAL_ADC_GetValue(&hadc1);
    HAL_GPIO_WritePin(GPIOD, GPIO_PIN_13, GPIO_PIN_SET);
    HAL_ADC_PollForConversion(&hadc1, HAL_MAX_DELAY);
    HAL_GPIO_WritePin(GPIOD, GPIO_PIN_14, GPIO_PIN_SET);
    adc_value2 = (uint16_t)HAL_ADC_GetValue(&hadc1);
    HAL_GPIO_WritePin(GPIOD, GPIO_PIN_15, GPIO_PIN_SET);
    HAL_ADC_PollForConversion(&hadc1, HAL_MAX_DELAY);
    adc_value3 = (uint16_t)HAL_ADC_GetValue(&hadc1);

    HAL_ADC_Stop(&hadc1);

    uint16_to_str(adc_value1, adc1Buffer);
    uint16_to_str(adc_value2, adc2Buffer);
    uint16_to_str(adc_value3, adc3Buffer);

    UART_SendString(" R:");
    UART_SendString(adc1Buffer);
    UART_SendString(" S:");
    UART_SendString(adc2Buffer);
    UART_SendString(" L:");
    UART_SendString(adc3Buffer);
    UART_SendString("\r\n");

}

void uint16_to_str(uint16_t value, char *buffer) {
	uint8_t i = 0;
	if (value == 0) {
		buffer[i++] = '0';
	} else {
		char temp[6];
	uint8_t j = 0;
	while (value > 0)
	{ temp[j++] = (value % 10) + '0';
	value /= 10;
	}
	while (j > 0)
	{
		buffer[i++] = temp[--j];
	}
	}
	buffer[i] = '\0'; }

