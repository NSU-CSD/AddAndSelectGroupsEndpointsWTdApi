package com.krpo.krpo.MLstub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class MLResponseDTO {

    @JsonProperty("user_id")
    public String userId; // not ID but username, containing '@'

    @JsonProperty("message")
    public String message;

    @JsonProperty("analysis_mask")
    public String analysisMask;

    @JsonProperty("toxicity_ratio")
    public double toxicityRatio;

    @JsonProperty("publication_time")
    public String publicationTime;

    public MLResponseDTO(String userId, String message, String publicationTime) {
        this.userId = userId;
        this.message = message;
        this.publicationTime = publicationTime;
    }
}
