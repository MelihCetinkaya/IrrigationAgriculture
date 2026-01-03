package com.springexample.irrigationagriculture.dto.aiDto;

public class PlantSuitabilityResponse {
    private String plantName;
    private String suitabilityScore;
    private String analysis;

    public PlantSuitabilityResponse() {
    }

    public PlantSuitabilityResponse(String plantName, String suitabilityScore, String analysis) {
        this.plantName = plantName;
        this.suitabilityScore = suitabilityScore;
        this.analysis = analysis;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getSuitabilityScore() {
        return suitabilityScore;
    }

    public void setSuitabilityScore(String suitabilityScore) {
        this.suitabilityScore = suitabilityScore;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
