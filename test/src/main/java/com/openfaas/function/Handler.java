package com.openfaas.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Handler extends com.openfaas.model.AbstractHandler {

    public IResponse Handle(IRequest req) {
        String request = req.getBody();
        Response res = new Response();
        try {
            List<Fare> faresList = (new ObjectMapper()).readValue(request, new TypeReference<>() {
            });
            Map<Integer, Integer> frequencyPerQuarter = initializeMap();
            faresList.forEach(fare -> enrichQuarterCount(frequencyPerQuarter, fare));
            res.setBody((new ObjectMapper()).writeValueAsString(frequencyPerQuarter));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static Map<Integer, Integer> initializeMap() {
        Map<Integer, Integer> frequencyPerQuarter = new HashMap<>();
        frequencyPerQuarter.put(1, 0);
        frequencyPerQuarter.put(2, 0);
        frequencyPerQuarter.put(3, 0);
        frequencyPerQuarter.put(4, 0);
        return frequencyPerQuarter;
    }

    private static void enrichQuarterCount(Map<Integer, Integer> frequencyPerQuarter, Fare fare) {
        double latitude = fare.getPickupLatitude();
        double longitude = fare.getPickupLongitude();
        if (longitude >= -73.99116516113281 && longitude <= -68.7915620803833 && latitude >= 39.72673797607412 && latitude <= 44.371944427490234) {
            frequencyPerQuarter.put(1, frequencyPerQuarter.get(1) + 1);
        } else if (longitude >= -68.7915620803833 && longitude <= -63.59195899963379 && latitude >= 39.72673797607412 && latitude <= 44.371944427490234) {
            frequencyPerQuarter.put(2, frequencyPerQuarter.get(2) + 1);
        } else if (longitude >= -73.99116516113281 && longitude <= -68.7915620803833 && latitude >= 35.0815315246582 && latitude <= 39.72673797607412) {
            frequencyPerQuarter.put(3, frequencyPerQuarter.get(3) + 1);
        } else {
            frequencyPerQuarter.put(4, frequencyPerQuarter.get(4) + 1);
        }
    }


}
