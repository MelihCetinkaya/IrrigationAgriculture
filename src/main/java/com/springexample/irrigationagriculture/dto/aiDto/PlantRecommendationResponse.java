package com.springexample.irrigationagriculture.dto.aiDto;

import java.util.List;

public class PlantRecommendationResponse {
    private List<String> recommendedPlants;
    private String explanation;

    public PlantRecommendationResponse() {
    }

    public PlantRecommendationResponse(List<String> recommendedPlants, String explanation) {
        this.recommendedPlants = recommendedPlants;
        this.explanation = explanation;
    }

    public List<String> getRecommendedPlants() {
        return recommendedPlants;
    }

    public void setRecommendedPlants(List<String> recommendedPlants) {
        this.recommendedPlants = recommendedPlants;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
