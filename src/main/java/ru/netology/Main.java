package ru.netology;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            // Создаем HTTP-клиент с настройками
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)    // максимальное время ожидания подключения к серверу
                            .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                            .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                            .build())
                    .build();

            // Создаем HTTP-запрос
            HttpGet request = new HttpGet("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");

            // Выполняем запрос
            CloseableHttpResponse response = httpClient.execute(request);

            // Получаем содержимое ответа
            String responseBody = EntityUtils.toString(response.getEntity());

            // Закрываем соединение
            response.close();
            httpClient.close();

            // Преобразуем JSON в список объектов CatFact
            ObjectMapper objectMapper = new ObjectMapper();
            List<CatFact> catFacts = objectMapper.readValue(responseBody, objectMapper.getTypeFactory().constructCollectionType(List.class, CatFact.class));

            // Фильтруем факты, у которых upvotes не равно null
            List<CatFact> filteredCatFacts = catFacts.stream()
                    .filter(catFact -> catFact.getUpvotes() != null && catFact.getUpvotes() > 0)
                    .collect(Collectors.toList());

            // Выводим результат на экран
            filteredCatFacts.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}