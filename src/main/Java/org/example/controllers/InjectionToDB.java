package org.example.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InjectionToDB {
    public static void main(String[] args) throws IOException {
        String URL = "http://localhost:8080/gns/add";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<String> jsonList = new ArrayList<>();
        File folder = new File("/C:/Users/a.kuzmin/Desktop/ГНС");
        File[] files = folder.listFiles();

        for (File file : files) {
            System.out.println(file.getName());
            BufferedReader reader = new BufferedReader(new FileReader(file));


            String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
            restTemplate.postForObject(URL, requestEntity, String.class);
            System.out.println(file.getName() + " finished");
        }

    }
}
