package xyz.oiio.open_ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Message {
    private String role;
    private String content;
}
