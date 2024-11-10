package xyz.oiio.open_ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import xyz.oiio.open_ai.dto.ChatGPTRequest;
import xyz.oiio.open_ai.dto.ChatGPTResponse;
import xyz.oiio.open_ai.service.ChatGPTService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bot")
public class CustomBotController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    private final ChatGPTService chatGPTService;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt")String prompt){
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGPTResponse chatGPTResponse =  template.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithGPTStream(@RequestParam(name = "prompt")String prompt) {
        return chatGPTService.getChatGPTResponseAsStream(prompt);
    }
}
