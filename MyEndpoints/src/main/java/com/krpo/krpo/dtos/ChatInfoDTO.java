package com.krpo.krpo.dtos;

import org.springframework.lang.NonNull;

import java.io.Serializable;

public class ChatInfoDTO implements Serializable {
    @NonNull
    public long chatId;
    @NonNull
    public String title;

    public ChatInfoDTO(long chatId, String title) {
        this.title = title;
        this.chatId = chatId;
    }
}
