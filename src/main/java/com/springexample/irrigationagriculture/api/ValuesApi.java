package com.springexample.irrigationagriculture.api;

import com.springexample.irrigationagriculture.entity.enums.IrrigationMode;
import com.springexample.irrigationagriculture.service.ValuesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/values")
public class ValuesApi {

    private final ValuesService valuesService;

    public ValuesApi(ValuesService valuesService) {
        this.valuesService = valuesService;
    }

    @GetMapping("/enterRoom")
    public Boolean enterValuesRoom(){

        return valuesService.enterValuesRoom();

    }

    @GetMapping("/showValues")
    public ResponseEntity<String> showValues(@RequestHeader("Authorization") String token,@RequestParam String factor){

        return ResponseEntity.ok(valuesService.showValues(token,factor));

    }

    @GetMapping("/checkFactor")
    public ResponseEntity<Boolean> checkFactor(@RequestHeader("Authorization") String token,@RequestParam String factor){

        return ResponseEntity.ok(valuesService.checkActivity(token,factor));

    }

    @GetMapping("/thresholds")
    public ResponseEntity<String> getThresholds(@RequestHeader("Authorization") String token,@RequestParam String factor,@RequestParam String minmax){

        return ResponseEntity.ok(valuesService.getMinMaxThreshold(token,factor,minmax));

    }

    @GetMapping("/minmaxTime")
    public ResponseEntity<String> getMinmaxTime(@RequestHeader("Authorization") String token,@RequestParam String factor,@RequestParam String minmax){

        return ResponseEntity.ok(valuesService.getMinMaxTime(token,factor,minmax));

    }

    @GetMapping("/mode")
    public ResponseEntity<IrrigationMode> getMode(@RequestHeader("Authorization") String token){

        return ResponseEntity.ok(valuesService.checkMode(token));

    }

    @GetMapping("/notification")
    public ResponseEntity<Boolean> getNotification(@RequestHeader("Authorization") String token,@RequestParam String factor,@RequestParam String notifType){

        return ResponseEntity.ok(valuesService.checkNotification(token,factor,notifType));

    }

    @GetMapping("/flowTime")
    public ResponseEntity<Double> getFlowTime(@RequestHeader("Authorization") String token){

        return ResponseEntity.ok(valuesService.getFlowTime(token));

    }


}
