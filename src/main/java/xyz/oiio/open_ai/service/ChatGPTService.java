package xyz.oiio.open_ai.service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import xyz.oiio.open_ai.dto.ChatGPTRequest;
import xyz.oiio.open_ai.dto.Message;

@Service
public class ChatGPTService {
    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String openAiKey;

    public ChatGPTService(WebClient.Builder webClientBuilder, @Value("${openai.api.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public Flux<String> getChatGPTResponseAsStream(String userMessage) {
        ChatGPTRequest request = new ChatGPTRequest(
            "gpt-4o-mini",
            Collections.singletonList(Message.builder().role("user").content(userMessage).build())
                .toString()
        );

        return webClient.post()
            .header("Authorization", "Bearer " + openAiKey)
            .header("Content-Type", "application/json")
            .bodyValue(request)
            .retrieve()
            .bodyToFlux(String.class) // 스트리밍으로 응답 수신
            .doOnNext(text -> System.out.println("Received chunk: " + text)); // 각 스트리밍 청크를 출력
    }
}
