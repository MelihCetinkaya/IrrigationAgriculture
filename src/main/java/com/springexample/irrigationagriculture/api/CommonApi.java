package com.springexample.irrigationagriculture.api;


import com.springexample.irrigationagriculture.service.CommonFuncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/common")
public class CommonApi {

    private final CommonFuncService commonFuncService;

    public CommonApi(CommonFuncService commonFuncService) {
        this.commonFuncService = commonFuncService;
    }

    @PostMapping("/temp")
    public ResponseEntity<String>  changeTempStatus(@RequestHeader("Authorization") String token, @RequestParam Boolean status ) {
        return ResponseEntity.ok(commonFuncService.changeTempStatus(token,status));
    }

    @PostMapping("/irrigation")
    public ResponseEntity<String> enableIrrigation(@RequestHeader("Authorization") String token, @RequestParam String time)  {
        return ResponseEntity.ok(commonFuncService.enableIrrigation(token,time));
    }



    public void getCurrentCharger(@RequestHeader("Authorization") String token)  {
    }





}
