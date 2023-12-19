package com.krpo.krpo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyseToxicityDto {

    @NonNull
    @JsonProperty("chat_id")
    public long chatId;

    @NonNull
    @JsonProperty("number")
    public long number;

    @NonNull
    @JsonProperty("start_time")
    public String startTime;

    @NonNull
    @JsonProperty("end_time")
    public String endTime;

    @NonNull
    @JsonProperty("usernames")
    public List<String> userNames; //either 1 element "all" or platform usernames (in case of telegram prefixed with @)

}
