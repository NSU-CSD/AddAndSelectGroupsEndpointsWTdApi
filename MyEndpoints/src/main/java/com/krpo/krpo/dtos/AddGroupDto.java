package com.krpo.krpo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddGroupDto {

    @JsonProperty("platform_id")
    private int platform_id;

    @JsonProperty("chat_id")
    @NonNull
    private Long chat_id;

    @JsonProperty("user_login")
    private String user_login;

    @JsonProperty("service_account_login")
    private String service_account_login;

    @JsonProperty("service_account_password")
    private String service_account_password;

}
