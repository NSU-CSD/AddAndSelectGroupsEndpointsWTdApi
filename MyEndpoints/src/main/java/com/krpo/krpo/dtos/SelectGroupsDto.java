package com.krpo.krpo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SelectGroupsDto {

    @JsonProperty("platform_id")
    private int platform_id;

    @JsonProperty("user_login")
    private String user_login;

}
