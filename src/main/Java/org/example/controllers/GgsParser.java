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

public class GgsParser {
    public static void main(String[] args) throws IOException {
        for (int i = 717; i < 720; i++) {
            System.out.println(i);
            for (int j = 1; j < 105; j++) {
                File f = new File("gns" + i + "_" + j + ".txt");
                String jsonGNS = "{\"paging\":{\"pagenumber\":" + j + ",\"pagesize\":1000},\"sort\":null,\"filter\":" +
                        "{\"subtype_ref\":[106,107,108,109,111,110],\"name\":\"\",\"maingeographyfeature\":\"\",\"regions_ref\":[" +
                        i + "],\"districts_ref\":\"\",\"geoms\":[]},\"fields_include\":[\"regions_ref\",\"districts_ref\",\"" +
                        "epsgs_ref\",\"isFFPDLocation\",\"maingeographyfeature\",\"index\",\"mark\",\"class_ref\",\"signtype\",\"" +
                        "gngstationstatus_ref\",\"centertype\",\"heightsystem_ref\",\"epsgs\",\"accesscondition_ref\",\"externalid\"" +
                        ",\"guid\",\"subtype_ref\",\"name\", \"geom\"]}";
                String URL = "https://mdss.fppd.cgkipd.ru/api/v1/gngstation/Search";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonGNS, headers);
                try {
                    FileWriter writer = new FileWriter(f);
                    writer.write(Objects.requireNonNull(restTemplate.postForObject(URL, requestEntity, String.class)));
                    writer.close();
                } catch (IOException | HttpClientErrorException ex) {
                    FileWriter writer = new FileWriter(f);
                    writer.write("{\"errors\":[\"Число запрашиваемых объектов превышает 25000. Пожалуйста, уточните фильтр или перейдите к сценарию ручного ввода заявления.\"]}\" at " + i);
                }
            }
        }
    }

}