package com.krpo.krpo.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ErrorDto {

    private int code;
    private String message;

    public String toJSONString() {
        return "{\"code\":\"" + code + "\",\"message\":\"" + message + "\"}";
    }
}
