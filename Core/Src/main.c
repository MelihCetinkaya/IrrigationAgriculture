#include "main.h"
#include "string.h"
#include "stdio.h"

#include "dht11.h"

UART_HandleTypeDef huart2;
TIM_HandleTypeDef htim2;
TIM_HandleTypeDef htim3;

char rxBuffer[2] = {0,0};

uint8_t deger = 0;
uint8_t motorActive = 0;
uint8_t currentStep = 0;
uint8_t  valStep= 0;

float temperature;
float humidity;


void SystemClock_Config(void);
static void MX_GPIO_Init(void);
static void MX_UART_Init(void);
static void MX_TIM2_Init(void);
static void MX_TIM3_Init(void);


int main(void)
{

  HAL_Init();

  SystemClock_Config();
  DWT_Delay_Init();

  MX_GPIO_Init();
  MX_UART_Init();
  MX_TIM2_Init();
  MX_TIM3_Init();
  HAL_TIM_Base_Start(&htim3);
  __HAL_TIM_CLEAR_FLAG(&htim3, TIM_FLAG_UPDATE);

  memset(rxBuffer,0,sizeof(rxBuffer));
  if(HAL_UART_Receive_IT(&huart2, (uint8_t *)rxBuffer, 2)!=HAL_OK){
 		HAL_GPIO_WritePin(GPIOD, GPIO_PIN_13, GPIO_PIN_SET);
 		Error_Handler();
 	}

  while (1)
  {

	  if (__HAL_TIM_GET_FLAG(&htim3, TIM_FLAG_UPDATE))
	  {
	      __HAL_TIM_CLEAR_FLAG(&htim3, TIM_FLAG_UPDATE);

	      if (++valStep >= 2)
	      {
	          valStep = 0;

	          if (DHT11_Read(&temperature, &humidity))
	          {
	              Send_DHT11_UART(&temperature, &humidity);
	          }
	      }
	  }


  }

}


void SystemClock_Config(void)
{
  RCC_OscInitTypeDef RCC_OscInitStruct = {0};
  RCC_ClkInitTypeDef RCC_ClkInitStruct = {0};


  __HAL_RCC_PWR_CLK_ENABLE();
  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);


  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI;
  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
  RCC_OscInitStruct.HSICalibrationValue = RCC_HSICALIBRATION_DEFAULT;
  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSI;
  RCC_OscInitStruct.PLL.PLLM = 16;
  RCC_OscInitStruct.PLL.PLLN = 168;
  RCC_OscInitStruct.PLL.PLLP = RCC_PLLP_DIV2;
  RCC_OscInitStruct.PLL.PLLQ = 4;

  /* ((HSI/PLLM) X PLLN) PLLP = 84mHz
   * 84 000 000 / Prescaler(16800) = 0.0002
   * 0.0002 X Period(5000) = 1
   * HSI default 16 mHz
   */
  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
  {
    Error_Handler();
  }


  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV2;
  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;

  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_2) != HAL_OK)
  {
    Error_Handler();
  }
}

static void MX_GPIO_Init(void)
{
  GPIO_InitTypeDef GPIO_InitStruct = {0};

  __HAL_RCC_GPIOH_CLK_ENABLE();
  __HAL_RCC_GPIOD_CLK_ENABLE();
  __HAL_RCC_GPIOE_CLK_ENABLE();
  __HAL_RCC_GPIOB_CLK_ENABLE();

  GPIO_InitStruct.Pin = GPIO_PIN_12 | GPIO_PIN_13 | GPIO_PIN_14 | GPIO_PIN_15;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_FREQ_HIGH;
  HAL_GPIO_Init(GPIOD, &GPIO_InitStruct);

  GPIO_InitStruct.Pin = GPIO_PIN_3;
  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Pull = GPIO_NOPULL;
  GPIO_InitStruct.Speed = GPIO_SPEED_HIGH;
  HAL_GPIO_Init(GPIOE, &GPIO_InitStruct);

}

static void MX_UART_Init(void){

	huart2.Instance=USART2;
	huart2.Init.BaudRate=115200;
	huart2.Init.HwFlowCtl=UART_HWCONTROL_NONE;
	huart2.Init.Mode=UART_MODE_TX_RX;
	huart2.Init.OverSampling=UART_OVERSAMPLING_16;
	huart2.Init.Parity=UART_PARITY_NONE;
	huart2.Init.StopBits=UART_STOPBITS_1;
	huart2.Init.WordLength=UART_WORDLENGTH_8B;

	if(HAL_UART_Init(&huart2)!=HAL_OK){

		Error_Handler();
	}

}

static void MX_TIM2_Init(void){

	TIM_ClockConfigTypeDef saatKaynakAyarla = {0};
	TIM_MasterConfigTypeDef masterAyarla = {0};

	htim2.Instance = TIM2;
	htim2.Init.Prescaler = 16800-1;
	htim2.Init.CounterMode = TIM_COUNTERMODE_UP;
	htim2.Init.Period = 5000-1;
	htim2.Init.ClockDivision=TIM_CLOCKDIVISION_DIV1;
	htim2.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;

	if(HAL_TIM_Base_Init(&htim2)!=HAL_OK){

		Error_Handler();
	}

	saatKaynakAyarla.ClockSource =TIM_CLOCKSOURCE_INTERNAL;
	if(HAL_TIM_ConfigClockSource(&htim2,&saatKaynakAyarla)!=HAL_OK){

		Error_Handler();
	}

	masterAyarla.MasterOutputTrigger = TIM_TRGO_RESET;
	masterAyarla.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;


	if(HAL_TIMEx_MasterConfigSynchronization(&htim2,&masterAyarla)!=HAL_OK){

			Error_Handler();
	}

}

static void MX_TIM3_Init(void)
{
    TIM_ClockConfigTypeDef sClockSourceConfig = {0};
    TIM_MasterConfigTypeDef sMasterConfig = {0};

    htim3.Instance = TIM3;
    htim3.Init.CounterMode = TIM_COUNTERMODE_UP;
    htim3.Init.Prescaler = 16800 - 1;
    htim3.Init.Period = 5000 - 1;
    htim3.Init.ClockDivision = TIM_CLOCKDIVISION_DIV1;
    htim3.Init.AutoReloadPreload = TIM_AUTORELOAD_PRELOAD_DISABLE;

    if (HAL_TIM_Base_Init(&htim3) != HAL_OK)
    {
        Error_Handler();
    }

    sClockSourceConfig.ClockSource = TIM_CLOCKSOURCE_INTERNAL;
    HAL_TIM_ConfigClockSource(&htim3, &sClockSourceConfig);

    sMasterConfig.MasterOutputTrigger = TIM_TRGO_RESET;
    sMasterConfig.MasterSlaveMode = TIM_MASTERSLAVEMODE_DISABLE;
    HAL_TIMEx_MasterConfigSynchronization(&htim3, &sMasterConfig);
}


void HAL_UART_RxCpltCallback(UART_HandleTypeDef *huart)
{

	if( rxBuffer[0] == 0){

		deger = (rxBuffer[1]);
		//HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_12);

	}

	else{

		deger = (rxBuffer[0]) * 10 + (rxBuffer[1] );
		 	//	HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_13);
		 }

		 if (deger == 0) {
		         memset(rxBuffer,0,sizeof(rxBuffer));
		         if(HAL_UART_Receive_IT(&huart2, (uint8_t *)rxBuffer, 2)!=HAL_OK){

		        	Error_Handler();
		         };
		         return;
		  }

		 currentStep = 0;
		 motorActive = 1;

		 HAL_GPIO_WritePin(GPIOE, GPIO_PIN_3, GPIO_PIN_SET);
		//HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_13);

		 __HAL_TIM_SET_COUNTER(&htim2, 0);
		 HAL_TIM_Base_Start_IT(&htim2);
		 memset(rxBuffer, 0, sizeof(rxBuffer));

		 if(HAL_UART_Receive_IT(&huart2, (uint8_t *)rxBuffer, 2)!=HAL_OK){
			Error_Handler();
		  }


}

void HAL_UART_TxCpltCallback(UART_HandleTypeDef *huart)
{
	HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_14);
}


void HAL_TIM_PeriodElapsedCallback(TIM_HandleTypeDef *htim)
{

   if(htim->Instance == TIM2){
	if (motorActive) {

	     currentStep++;

	     if (currentStep >= deger) {

	           HAL_GPIO_WritePin(GPIOE, GPIO_PIN_3, GPIO_PIN_RESET);
	           motorActive = 0;
	           HAL_TIM_Base_Stop_IT(&htim2);
	          // HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_14);

	            }
	     else{
	    	// HAL_GPIO_TogglePin(GPIOD, GPIO_PIN_15);

	     }

	        }}

}

void Error_Handler(void)
{
  __disable_irq();
  while (1)
  {
  }

}
#ifdef USE_FULL_ASSERT

void assert_failed(uint8_t *file, uint32_t line)
{

}
#endif
