package com.springexample.irrigationagriculture.service;

import com.springexample.irrigationagriculture.dto.aiDto.PlantRecommendationResponse;
import com.springexample.irrigationagriculture.dto.aiDto.PlantSuitabilityResponse;
import com.springexample.irrigationagriculture.dto.aiDto.PlantWateringResponse;
import com.springexample.irrigationagriculture.entity.Admin;
import com.springexample.irrigationagriculture.entity.UserSelections;
import com.springexample.irrigationagriculture.entity.enums.SensorType;
import com.springexample.irrigationagriculture.repository.AdminRepo;
import com.springexample.irrigationagriculture.repository.PersonRepo;
import com.springexample.irrigationagriculture.service.otherServices.HelperFuncs;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.springexample.irrigationagriculture.entity.SensorBuffer.*;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final HelperFuncs helperFuncs;
    private final PersonRepo personRepo;
    private final AdminRepo adminRepo;

    public AiService(ChatClient.Builder chatClientBuilder, HelperFuncs helperFuncs, PersonRepo personRepo, AdminRepo adminRepo) {
        this.chatClient = chatClientBuilder.build();
        this.helperFuncs = helperFuncs;
        this.personRepo = personRepo;
        this.adminRepo = adminRepo;
    }


    public PlantRecommendationResponse recommendPlants(String token,UserSelections userSelections) {

        if (!helperFuncs.checkUser(token)) {
            return null;
        }

        StringBuilder climateInfo = new StringBuilder("Based on the following climate data:\n");
        boolean hasData = false;

        int[] array;
        array= userSelections.getArr();


        if (array[0]==1) {
            List<Double> list = getLast(userSelections.getNumber(),SensorType.TEMP);
            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Temperature: Average %.2f°C (Range: %.2f°C - %.2f°C)\n", avg, min, max));
            hasData = true;
        }

        if (array[1]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.HUM);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Humidity: Average %.2f%% (Range: %.2f%% - %.2f%%)\n", avg, min, max));
            hasData = true;
        }

        if (array[2]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.SOIL);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Soil Moisture: Average %.2f%% (Range: %.2f%% - %.2f%%)\n", avg, min, max));
            hasData = true;
        }

        if (array[3]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.RAIN);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Rain: Average %.2f mm (Range: %.2f mm - %.2f mm)\n", avg, min, max));
            hasData = true;
        }

        if (!hasData) {
            return new PlantRecommendationResponse(
                    Arrays.asList("No data provided"),
                    "Please provide at least one climate parameter (temperature, humidity, soil moisture, or rain)."
            );
        }


        String prompt = climateInfo.toString() + "\n" +
                "Please recommend 3-5 suitable plants that can grow well in this climate. " +
                "Provide your answer in the following format:\n" +
                "PLANTS: plant1, plant2, plant3\n" +
                "EXPLANATION: Provide a brief, concise explanation (2-3 sentences maximum) of why these plants are suitable for this climate.";

        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return parseAiResponse(aiResponse);
    }

    private PlantRecommendationResponse parseAiResponse(String aiResponse) {
        try {
            String[] parts = aiResponse.split("EXPLANATION:");

            String plantsSection = "";
            String explanation = "";

            if (parts.length >= 1) {
                plantsSection = parts[0].replace("PLANTS:", "").trim();
            }

            if (parts.length >= 2) {
                explanation = parts[1].trim();
            }


            List<String> plants = Arrays.stream(plantsSection.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());


            if (plants.isEmpty()) {
                plants = Arrays.asList("Various suitable plants");
                explanation = aiResponse;
            }

            return new PlantRecommendationResponse(plants, explanation);

        } catch (Exception e) {

            return new PlantRecommendationResponse(
                    Arrays.asList("Various suitable plants"),
                    aiResponse
            );
        }
    }

    public PlantSuitabilityResponse checkPlantSuitability(String token,String plantName,UserSelections userSelections) {

        if (!helperFuncs.checkUser(token)) {
            return null;
        }

        StringBuilder climateInfo = new StringBuilder();
        boolean hasData = false;

        int[] array;
        array= userSelections.getArr();

        if (array[0]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.TEMP);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Temperature: Average %.2f°C (Range: %.2f°C - %.2f°C)\n", avg, min, max));
            hasData = true;
        }

        if (array[1]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.HUM);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Humidity: Average %.2f%% (Range: %.2f%% - %.2f%%)\n", avg, min, max));
            hasData = true;
        }

        if (array[2]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.SOIL);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Soil Moisture: Average %.2f%% (Range: %.2f%% - %.2f%%)\n", avg, min, max));
            hasData = true;
        }

        if (array[3]==1) {

            List<Double> list = getLast(userSelections.getNumber(),SensorType.RAIN);

            double avg = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            double min = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(0.0);
            double max = list.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);

            climateInfo.append(String.format("Rain: Average %.2f mm (Range: %.2f mm - %.2f mm)\n", avg, min, max));
            hasData = true;
        }

        if (!hasData) {
            return new PlantSuitabilityResponse(
                    plantName,
                    "Unknown",
                    "No climate data provided. Please provide at least one climate parameter (temperature, humidity, soil moisture, or rain)."
            );
        }

        String prompt = String.format(
                "Analyze the suitability of growing '%s' in the following climate conditions:\n%s\n" +
                        "Please provide your analysis in the following format:\n" +
                        "SCORE: [Provide a suitability score like 'Excellent', 'Good', 'Moderate', 'Poor', or 'Not Suitable']\n" +
                        "ANALYSIS: [Provide a brief, concise analysis (2-3 sentences maximum) of how well this plant can grow in these conditions]",
                plantName, climateInfo.toString()
        );

        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();


        return parseSuitabilityResponse(plantName, aiResponse);
    }

    private PlantSuitabilityResponse parseSuitabilityResponse(String plantName, String aiResponse) {
        try {
            String score = "Moderate";
            String analysis = "";

            if (aiResponse.contains("SCORE:")) {
                String[] scoreParts = aiResponse.split("ANALYSIS:");
                if (scoreParts.length > 0) {
                    score = scoreParts[0].replace("SCORE:", "").trim();
                }
            }

            if (aiResponse.contains("ANALYSIS:")) {
                String[] analysisParts = aiResponse.split("ANALYSIS:");
                if (analysisParts.length > 1) {
                    analysis = analysisParts[1].trim();
                }
            }

            if (analysis.isEmpty()) {
                analysis = aiResponse;
            }

            return new PlantSuitabilityResponse(plantName, score, analysis);

        } catch (Exception e) {

            return new PlantSuitabilityResponse(
                    plantName,
                    "Unknown",
                    aiResponse
            );
        }
    }

    public List<PlantWateringResponse>manageIrrigation(String token, String plantName, UserSelections userSelections) {

        if (!helperFuncs.checkUser(token)) {
            return null;
        }

        StringBuilder climateInfo = new StringBuilder("Based on the following climate data:\n");
        boolean hasData = false;
        int[] array = userSelections.getArr();

        if (array[0] == 1) {

            List<Double> list = getLast(userSelections.getNumber(), SensorType.TEMP);

            double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double min = list.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double max = list.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

            climateInfo.append(
                    String.format(
                            "Temperature: Average %.2f%% (Range: %.2f%% - %.2f%%)\n",
                            avg, min, max
                    )
            );

            hasData = true;
        }
        if (array[1] == 1) {

            List<Double> list = getLast(userSelections.getNumber(), SensorType.HUM);

            double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double min = list.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double max = list.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

            climateInfo.append(
                    String.format(
                            "Humidity: Average %.2f%% (Range: %.2f%% - %.2f%%)\n",
                            avg, min, max
                    )
            );
            hasData = true;
        }
        if (array[2] == 1) {

            List<Double> list = getLast(userSelections.getNumber(), SensorType.SOIL);

            double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double min = list.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double max = list.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

            climateInfo.append(
                    String.format(
                            "Soil Moisture: Average %.2f%% (Range: %.2f%% - %.2f%%)\n",
                            avg, min, max
                    )
            );
            hasData = true;
        }
        if (array[3] == 1) {

            List<Double> list = getLast(userSelections.getNumber(), SensorType.RAIN);

            double avg = list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double min = list.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            double max = list.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

            climateInfo.append(
                    String.format(
                            "Weather(rain): Average %.2f%% (Range: %.2f%% - %.2f%%)\n",
                            avg, min, max
                    )
            );
            hasData = true;
        }
        if (!hasData) {
            System.out.println("no data provided");
            return null;
        }

        String prompt = String.format(
                """
                You are an agricultural assistant.
    
                Based on the plant '%s' and the following climate conditions:
                %s
    
                Determine values for every climate conditions for this plant and recommend watering time.If
                one climate condition provided you will be produce one output format,if two -> two output format,three->three
                format,four->four format.Shortly you gonna create output format or formats for every climate condition or
                conditions provided.Determine the IDEAL climate ranges.Then compare the provided climate data with the ideal
                ranges and decide the watering time accordingly.
           
                 IMPORTANT:
                 - Ideal values MUST be determined by you (do NOT reuse given values)
                 - IDEAL_MIN < IDEAL_MAX
                 - Watering time MUST be between 0 and 5 seconds
                 - Watering time MUST change depending on the condition
                 - Provide exact numeric values
                 - Do NOT add explanations
                 - Do NOT add extra text 
                 - Output ONLY the specified format
                 
                 CRITICAL RULE:
                 - ONLY generate output for climate conditions that appear in the provided climate data.
                 - DO NOT generate output for any climate condition that is not listed above.
                OUTPUT FORMAT:
                CLIMATE CONDITION: <One of them :temperature,humidity,soil moisture,rain>
                IDEAL_MIN: <number>
                IDEAL_MAX: <number>
                WATERING_TIME:
                - BELOW_MIN: <seconds>s
                - AVERAGE: <seconds>s
                - ABOVE_MAX: <seconds>s
               """,
                plantName,
                climateInfo.toString()
        );

        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
        System.out.println("===== AI RAW RESPONSE START =====");
        System.out.println(aiResponse);
        System.out.println("===== AI RAW RESPONSE END =====");
        return parseWateringResponse(aiResponse);
    }

    private List<PlantWateringResponse> parseWateringResponse(String aiResponse) {

        List<PlantWateringResponse> responses = new ArrayList<>();

        String[] blocks = aiResponse.split("CLIMATE CONDITION:");

        for (String block : blocks) {

            block = block.trim();
            if (block.isEmpty()) continue;

            String[] lines = block.split("\n");

            String climateCondition = lines[0].trim().toLowerCase();

            double min = 0, max = 0;
            int belowMin = 0, average = 0, aboveMax = 0;

            for (String rawLine : lines) {

                String line = rawLine.trim().toUpperCase();

                if (line.startsWith("IDEAL_MIN")) {
                    min = extractLabeledDouble(rawLine);
                }
                else if (line.startsWith("IDEAL_MAX")) {
                    max = extractLabeledDouble(rawLine);
                }
                else if (line.contains("BELOW_MIN")) {
                    belowMin = extractInt(rawLine);
                }
                else if (line.contains("AVERAGE")) {
                    average = extractInt(rawLine);
                }
                else if (line.contains("ABOVE_MAX")) {
                    aboveMax = extractInt(rawLine);
                }
            }

            if (min > max) {
                double tmp = min;
                min = max;
                max = tmp;
            }

            PlantWateringResponse response = new PlantWateringResponse(climateCondition, min, max, belowMin, average, aboveMax);
            responses.add(response);
            setChanges(response);
        }

        return responses;
    }


    private double extractLabeledDouble(String line) {
        Matcher m = Pattern.compile(":(\\s*)(\\d+(\\.\\d+)?)").matcher(line);
        if (m.find()) {
            return Double.parseDouble(m.group(2));
        }
        return 0.0;
    }

    private int extractInt(String line) {
        Matcher m = Pattern.compile("(\\d+)").matcher(line);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }


    private void setChanges(PlantWateringResponse response){

        Admin admin = (Admin) personRepo.findByUsername("admin1").orElseThrow();

        if(Objects.equals(response.getClimateCondition(),"temperature")){

            admin.getPlantHouse().getAmounts().setMinTemp(response.getMinValue());
            admin.getPlantHouse().getAmounts().setMaxTemp(response.getMaxValue());
            admin.getPlantHouse().getAmounts().setMinTempTime(response.getWateringBelowMin());
            admin.getPlantHouse().getAmounts().setMaxTempTime(response.getWateringAboveMax());
            admin.getPlantHouse().getAmounts().setMidTempTime(response.getWateringAtAvg());

        }

        else if(Objects.equals(response.getClimateCondition(),"humidity")){

            admin.getPlantHouse().getAmounts().setMinHum(response.getMinValue());
            admin.getPlantHouse().getAmounts().setMaxHum(response.getMaxValue());
            admin.getPlantHouse().getAmounts().setMinHumTime(response.getWateringBelowMin());
            admin.getPlantHouse().getAmounts().setMaxHumTime(response.getWateringAboveMax());
            admin.getPlantHouse().getAmounts().setMidHumTime(response.getWateringAtAvg());

        }

        else if(Objects.equals(response.getClimateCondition(),"soil moisture")){

            admin.getPlantHouse().getAmounts().setMinSoil(response.getMinValue());
            admin.getPlantHouse().getAmounts().setMaxSoil(response.getMaxValue());
            admin.getPlantHouse().getAmounts().setMinSoilTime(response.getWateringBelowMin());
            admin.getPlantHouse().getAmounts().setMaxSoilTime(response.getWateringAboveMax());
            admin.getPlantHouse().getAmounts().setMidSoilTime(response.getWateringAtAvg());

        }

        else if(Objects.equals(response.getClimateCondition(),"rain")){

            admin.getPlantHouse().getAmounts().setMinWth(response.getMinValue());
            admin.getPlantHouse().getAmounts().setMaxWth(response.getMaxValue());
            admin.getPlantHouse().getAmounts().setMinWthTime(response.getWateringBelowMin());
            admin.getPlantHouse().getAmounts().setMaxWthTime(response.getWateringAboveMax());
            admin.getPlantHouse().getAmounts().setMidWthTime(response.getWateringAtAvg());

        }
        else{
            System.out.println("wrong parameter");
            return;
        }

        adminRepo.save(admin);
    }


    public static List<Double> getLast(int count,SensorType sensorType) {

        Deque<Double> queue;
        int size;

        if(count == 0){
            System.out.println("please provide a number");
            return null;
        }

        if(sensorType ==  SensorType.TEMP){

            queue = tempValues;
            size = tempValues.size();
        }
        else if(sensorType ==  SensorType.HUM){

            queue = humValues;
            size = humValues.size();
        }
         else if(sensorType ==  SensorType.SOIL){

            queue = soilValues;
            size = soilValues.size();
        }
         else if(sensorType ==  SensorType.RAIN){

            queue = rainValues;
            size = rainValues.size();
        }

        else{
            System.out.println("Invalid sensor type");
            return null;
        }

        int startIndex = Math.max(0, size - count);
        List<Double> result = new ArrayList<>(count);

        int i = 0;
        for (Double val : queue) {
            if (i++ >= startIndex) {
                result.add(val);
            }
        }
        return result;
    }

}
