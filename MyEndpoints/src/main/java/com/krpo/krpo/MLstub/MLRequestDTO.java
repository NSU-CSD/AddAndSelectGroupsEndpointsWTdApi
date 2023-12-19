package com.krpo.krpo.MLstub;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.krpo.krpo.controllers.helpers.InfoHelper;
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
public class MLRequestDTO {

    @JsonProperty("message_info_list")
    List<InfoHelper.MessageInfo> messageInfoList;

}
