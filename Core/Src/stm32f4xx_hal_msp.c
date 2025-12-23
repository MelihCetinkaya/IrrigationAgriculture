#include "main.h"

void HAL_MspInit(void)
{

  __HAL_RCC_SYSCFG_CLK_ENABLE();
  __HAL_RCC_PWR_CLK_ENABLE();

}


void HAL_UART_MspInit(UART_HandleTypeDef *huart)
{
    if (huart->Instance == USART2)
    {
        __HAL_RCC_USART2_CLK_ENABLE();
        __HAL_RCC_GPIOA_CLK_ENABLE();

        GPIO_InitTypeDef UartPinTanimla = {0};


        UartPinTanimla.Pin = GPIO_PIN_2 | GPIO_PIN_3;
        UartPinTanimla.Mode = GPIO_MODE_AF_PP;
        UartPinTanimla.Pull = GPIO_PULLUP;
        UartPinTanimla.Speed = GPIO_SPEED_FREQ_VERY_HIGH;
        UartPinTanimla.Alternate = GPIO_AF7_USART2;
        HAL_GPIO_Init(GPIOA, &UartPinTanimla);

       HAL_NVIC_SetPriority(USART2_IRQn, 0, 0);
       HAL_NVIC_EnableIRQ(USART2_IRQn);
    }
}

void HAL_UART_MspDeInit(UART_HandleTypeDef *huart)
{

	__HAL_RCC_USART2_CLK_DISABLE();
	HAL_GPIO_DeInit(GPIOA, GPIO_PIN_2 | GPIO_PIN_3);

}


void HAL_TIM_Base_MspInit(TIM_HandleTypeDef *htim)
{

	if(htim->Instance == TIM2){
	__HAL_RCC_TIM2_CLK_ENABLE();
	HAL_NVIC_SetPriority(TIM2_IRQn , 0 , 1);
	HAL_NVIC_EnableIRQ(TIM2_IRQn);}

	if(htim->Instance == TIM3){
		__HAL_RCC_TIM3_CLK_ENABLE();
		}

}



void HAL_TIM_Base_MspDeInit(TIM_HandleTypeDef *htim)
{
	 if (htim->Instance == TIM2)
	    {
	        __HAL_RCC_TIM2_CLK_DISABLE();
	        HAL_NVIC_DisableIRQ(TIM2_IRQn);
	    }

	    if (htim->Instance == TIM3)
	    {
	        __HAL_RCC_TIM3_CLK_DISABLE();

	    }
}


