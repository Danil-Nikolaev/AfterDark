package com.nikolaev.AfterDarkCandleBot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AfterDarkAPI {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private final String mainUrlApi = "http://localhost:8080/api/afterdark/";

    public AfterDarkAPI(@Autowired RestTemplate restTemplate, @Autowired ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ArrayNode getAllOrdersByUser(long chatId) {
        String url = mainUrlApi + "order/findByUser?login=" + String.valueOf(chatId);
        ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(url, ArrayNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    /*
     * Get all candles in AfterDarkApi and retun ArrayNode
     */
    public ArrayNode getCandles(long chatId) {
        String url = mainUrlApi + "candle";
        ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(url, ArrayNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            ArrayNode candles = response.getBody();
            ArrayNode result = objectMapper.createArrayNode();
            for (JsonNode candle : candles) {
                if (candle.get("custom").asText().equals("false")
                        || candle.get("name").asText().equals(String.valueOf(chatId))) {
                    result.add(candle);
                }
            }
            return result;
        }
        return null;
    }

    /*
     * This method need for find user in API
     * by chat id. In API user login = chatId
     */
    public Boolean findUser(long chatId) {
        String url = mainUrlApi + "user/find?login=" + String.valueOf(chatId);
        ResponseEntity<Boolean> response = this.restTemplate.getForEntity(url, Boolean.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    /*
     * This method sign up new User.
     */
    public void registerNewUser(long chatId, String name) {
        String url = mainUrlApi + "user";
        Map<String, String> variables = new HashMap<>();
        variables.put("name", name);
        variables.put("login", String.valueOf(chatId));
        variables.put("password", "telegram");

        this.restTemplate.postForEntity(url, variables, null);
    }

    /*
     * This is method need for find user by user login
     */
    public JsonNode findUserByLogin(long chatId) {
        String url = mainUrlApi + "user/findUser?login=" + String.valueOf(chatId);
        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }

    /*
     * This is method need for find Basket by id
     */
    public JsonNode findBasketById(long id) {
        final String url = mainUrlApi + "basket/" + String.valueOf(id);

        ResponseEntity<JsonNode> response = this.restTemplate.getForEntity(url, JsonNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }

        return null;
    }

    public void deleteCandlesInBusket(long chatId) {
        JsonNode user = findUserByLogin(chatId);
        String id = String.valueOf(user.get("id"));

        String url = mainUrlApi + "basket/" + id;

        ObjectNode basket = (ObjectNode) findBasketById(Long.valueOf(id));
        basket.set("candles", null);
        this.restTemplate.put(url, basket, id);

    }

    /*
     * This method in start find user by chatId,
     * then add candle in basket.
     */
    public void addCandleInBasket(long chatId, String candleId) {
        boolean inCandles = false;

        JsonNode user = findUserByLogin(chatId);
        String id = String.valueOf(user.get("id"));

        String url = mainUrlApi + "basket/" + id;

        JsonNode basket = findBasketById(Long.valueOf(id));
        JsonNode candles = basket.get("candles");

        List<Map<String, String>> urlVariables = new ArrayList<>();
        Map<String, String> mapCandle;

        if (candles.isArray()) {
            for (JsonNode candle : candles) {
                id = String.valueOf(candle.get("id"));
                if (id.equals(candleId))
                    inCandles = true;

                mapCandle = new HashMap<>();
                mapCandle.put("id", id);
                urlVariables.add(mapCandle);
            }
        }

        if (!inCandles) {
            mapCandle = new HashMap<>();
            mapCandle.put("id", candleId);
            urlVariables.add(mapCandle);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("candles", urlVariables);
        this.restTemplate.put(url, map, id);

    }

    /*
     * This method in start find user by chatId,
     * then delete candle from basket.
     */
    public void deleteCandleFromBasket(long chatId, String candleId) {
        JsonNode user = findUserByLogin(chatId);
        String id = String.valueOf(user.get("id"));

        String url = mainUrlApi + "basket/" + id;

        JsonNode basket = findBasketById(Long.valueOf(id));
        JsonNode candles = basket.get("candles");

        List<Map<String, String>> urlVariables = new ArrayList<>();
        Map<String, String> mapCandle;

        if (candles.isArray()) {
            for (JsonNode candle : candles) {
                id = String.valueOf(candle.get("id"));
                if (!id.equals(candleId)) {
                    mapCandle = new HashMap<>();
                    mapCandle.put("id", id);
                    urlVariables.add(mapCandle);
                }
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put("candles", urlVariables);
        this.restTemplate.put(url, map, id);
    }

    /*
     * This method get all candles in basket user
     */
    public ArrayNode getCandlesInBasket(long chatId) {
        JsonNode user = findUserByLogin(chatId);
        String id = String.valueOf(user.get("id"));
        JsonNode basket = findBasketById(Long.valueOf(id));
        JsonNode candles = basket.get("candles");

        ArrayNode arrayNode = objectMapper.createArrayNode();
        if (candles.isArray()) {

            for (JsonNode candle : candles) {
                arrayNode.add(candle);
            }
        }
        return arrayNode;
    }

    public ArrayNode getAllShapes() {
        String url = mainUrlApi + "shape";
        ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(url, ArrayNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public ArrayNode getAllSmell() {
        String url = mainUrlApi + "smell";
        ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(url, ArrayNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public ArrayNode getAllColor() {
        String url = mainUrlApi + "colorshape";
        ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(url, ArrayNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public ArrayNode getAllWick() {
        String url = mainUrlApi + "wick";
        ResponseEntity<ArrayNode> response = this.restTemplate.getForEntity(url, ArrayNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    public JsonNode createNewCandle(long chatId, Map<String, Long> candleMap) {
        candleMap.put("name", chatId);
        JsonNode candle = findCandle(candleMap);
        if (candle != null) {
            addCandleInBasket(chatId, candle.get("id").asText());
        }
        return null;
    }

    public JsonNode createNewOrder(JsonNode candle) {
        String url = mainUrlApi + "order";
        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(url, candle, JsonNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

    private JsonNode findCandle(Map<String, Long> candleMap) {
        String url = mainUrlApi + "candle/findCandle";
        ResponseEntity<JsonNode> response = this.restTemplate.postForEntity(url, candleMap, JsonNode.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        return null;
    }

}
