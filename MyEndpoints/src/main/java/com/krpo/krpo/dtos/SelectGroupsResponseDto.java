package com.krpo.krpo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class SelectGroupsResponseDto {

    @JsonProperty("format_version")
    private String format_version;

    @JsonProperty("login")
    private String login;

    @JsonProperty("group_list")
    private List<GroupDto> group_list;

}