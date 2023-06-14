package org.example.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;
public class GgsRegion {

    public static void main(String[] args) throws IOException {

    for (int i = 1; i < 100; i++) {

        String URL = "http://localhost:8080/gns/msk";
        File f = new File("gns_msk_"+i+".kml");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonGNS = "{\"msk\":\""+i+"\" }";
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonGNS, headers);
        System.out.println(jsonGNS);

        try {
            restTemplate.postForObject(URL, requestEntity, String.class);
            FileWriter writer = new FileWriter(f);
            String kmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n";
            String kmlFooter = "</kml>";

            StringBuilder kmlPoints = new StringBuilder();

            JSONArray jsonList = new JSONArray(Objects.requireNonNull(restTemplate.postForObject(URL, requestEntity, String.class)));

            for (int j = 0; j < jsonList.length(); j++) {
                JSONObject json = jsonList.getJSONObject(j);
                double longitude = json.getJSONArray("coordinates").getDouble(0);
                double latitude = json.getJSONArray("coordinates").getDouble(1);
                String name = (json.getString("name") != null) ? json.getString("name") : "Нет данных";
                String index = (json.getString("index") != null) ? json.getString("index") : "Нет данных";
                String mark = (json.getString("mark") != null) ? json.getString("mark") : "Нет данных";
                String centerType = (json.getString("centerType") != null) ? json.getString("centerType") : "Нет данных";
                String sighType = (json.getString("sighType") != null) ? json.getString("sighType") : "Нет данных";
                String maingeographyfeature = (json.getString("maingeographyfeature") != null) ? json.getString("maingeographyfeature").replace("&", "и") : "Нет данных";
//
                String kmlPoint = "<Placemark>\n" +
                        "<name>" + name + "</name>\n" +
                        "<description>Индекс: " + index + ", марка: " + mark + ", тип центра: "
                        + centerType + ", оп. знак: " + sighType + ", кроки: " + maingeographyfeature + "</description>\n" +
                        "<Point>\n" +
                        "<coordinates>" + longitude + "," + latitude + "</coordinates>\n" +
                        "</Point>\n" +
                        "</Placemark>\n";

                kmlPoints.append(kmlPoint);
            }

            String kml = kmlHeader + kmlPoints.toString() + kmlFooter;
            writer.write(kml);
            writer.close();
        } catch (IOException | HttpClientErrorException ex) {
            FileWriter writer = new FileWriter(f);
            writer.write("{\"errors\":[\"Число запрашиваемых объектов превышает 25000. Пожалуйста, уточните фильтр или перейдите к сценарию ручного ввода заявления.\"]}\" at " + i);
        }
    }
}}

