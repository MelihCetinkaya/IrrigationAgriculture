package com.springexample.irrigationagriculture.api;

import com.springexample.irrigationagriculture.dto.aiDto.*;
import com.springexample.irrigationagriculture.entity.UserSelections;
import com.springexample.irrigationagriculture.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ai")
public class AiApi {

    private final AiService aiService;

    public AiApi(AiService aiService) {
        this.aiService = aiService;
    }


    @PostMapping("/recommendPlants")
    public ResponseEntity<PlantRecommendationResponse> recommendPlants(@RequestHeader("Authorization") String token,@RequestBody UserSelections selections) {
        PlantRecommendationResponse response = aiService.recommendPlants(token,selections);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/checkPlantSuitability")//plantName *****
    public ResponseEntity<PlantSuitabilityResponse> checkPlantSuitability(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "hyacinth") String plantName, @RequestBody UserSelections selections) {
        PlantSuitabilityResponse response = aiService.checkPlantSuitability(token,plantName,selections);
        System.out.println(selections.getNumber());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manageIrrigation")
    public ResponseEntity<List<PlantWateringResponse>> manageIrrigationWithAi(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "hyacinth") String plantName, @RequestBody UserSelections selections) {
        List<PlantWateringResponse> response = aiService.manageIrrigation(token,plantName,selections);
        System.out.println(plantName+selections.getNumber()+ Arrays.toString(selections.getArr()));
        return ResponseEntity.ok(response);
    }

}
