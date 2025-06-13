package com.examen.prog.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.*;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
public class ChatGptService {

  private static final String API_URL = "https://api.openai.com/v1/chat/completions";
  private static final String API_KEY = System.getenv("OPENAI_API_KEY");

  private final OkHttpClient client = new OkHttpClient();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public String getDefinitionInMalagasy(String word) {
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "gpt-3.5-turbo");

    List<Map<String, String>> messages = new ArrayList<>();
    messages.add(
        Map.of(
            "role",
            "user",
            "content",
            "Hazavao amin’ny teny malagasy ilay teny hoe \"" + word + "\"."
    ));
    requestBody.put("messages", messages);

    Request request =
        new Request.Builder()
            .url(API_URL)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .post(RequestBody.create(MediaType.get("application/json"), toJson(requestBody)))
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) throw new IOException("Erreur API: " + response);

      String responseBody = response.body().string();
      Map<String, Object> result = objectMapper.readValue(responseBody, Map.class);
      List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
      Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
      return (String) message.get("content");

    } catch (IOException e) {
      return "Tsy afaka namaly ny fangatahana: " + e.getMessage();
    }
  }

  private String toJson(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (IOException e) {
      throw new RuntimeException("Erreur JSON", e);
    }
  }
}
