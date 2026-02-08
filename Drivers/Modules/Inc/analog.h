#ifndef MODULES_INC_ANALOG_H_
#define MODULES_INC_ANALOG_H_

#include "stm32f4xx_hal.h"

void sendAnalogValues(void);
void uint16_to_str(uint16_t value, char *buffer);

#endif
