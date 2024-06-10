package com.example.HobbySync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

    private List<Choice> choices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Choice {
        private int index;
        private Message message;
    }
}
