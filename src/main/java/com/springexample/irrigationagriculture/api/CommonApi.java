package com.springexample.irrigationagriculture.api;


import com.springexample.irrigationagriculture.service.CommonFuncService;
import com.springexample.irrigationagriculture.service.IrrigationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
public class CommonApi {

    private final CommonFuncService commonFuncService;
    private final IrrigationService irrigationService;

    public CommonApi(CommonFuncService commonFuncService, IrrigationService irrigationService) {
        this.commonFuncService = commonFuncService;
        this.irrigationService = irrigationService;
    }

    @PostMapping("/irrigation")
    public ResponseEntity<String> enableIrrigation(@RequestHeader("Authorization") String token, @RequestParam String time)  {
        return ResponseEntity.ok(commonFuncService.enableIrrigation(token,time));
    }

    @PostMapping("/mode")
    public void changeMode(@RequestHeader("Authorization") String token,@RequestParam String mode)  {

        irrigationService.changeMode(token,mode);

    }
    @PostMapping("/factor")
    public void changeFactorEffect(@RequestHeader("Authorization") String token,@RequestParam Boolean activity,@RequestParam String factor)  {

        irrigationService.changeFactorEffect(token,activity,factor);

    }
    @PostMapping("/threshold")
    public void changeThresholds(@RequestHeader("Authorization") String token,@RequestParam String factor, @RequestParam String threshold, @RequestParam String minmax)  {

        irrigationService.changeThresholds(token,factor,threshold,minmax);

    }

    @PostMapping("/minmax")
    public void changeMinMaxTime(@RequestHeader("Authorization") String token,@RequestParam String factor, @RequestParam String minmax, @RequestParam int time)  {

        irrigationService.changeMinMaxTime(token,factor,minmax,time);

    }
    @PostMapping("/notification")//***** facctor
    public void changeNotification(@RequestHeader("Authorization") String token,@RequestParam String factor,@RequestParam String notifyType,@RequestParam Boolean activity)  {

        irrigationService.changeNotificationActivity(token,factor,notifyType,activity);

    }




    public void getCurrentCharger(@RequestHeader("Authorization") String token)  {
    }





}
