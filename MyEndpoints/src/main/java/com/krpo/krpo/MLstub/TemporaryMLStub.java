package com.krpo.krpo.MLstub;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krpo.krpo.controllers.helpers.InfoHelper;
import com.krpo.krpo.dtos.ErrorDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/ML", produces = MediaType.APPLICATION_JSON_VALUE)
public class TemporaryMLStub {

    @PostMapping(value = "/analyse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> analyseMessages(@RequestBody MLRequestDTO mlRequestDTO) {
        List<MLResponseDTO> response = new ArrayList<>();
        for (InfoHelper.MessageInfo messageInfo : mlRequestDTO.messageInfoList) {
            MLResponseDTO responseDTO = new MLResponseDTO(messageInfo.user_id, messageInfo.message, messageInfo.publication_time);

            //stub values --------------------
            responseDTO.toxicityRatio = 0.5;
            responseDTO.analysisMask = "atat";
            //--------------------------------

            response.add(responseDTO);
        }
        String jsonResponse = "";
        try {
            jsonResponse = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(response);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());

            String errorMessage = "InternalRequestError: internal json mapping error: " + e.getMessage();
            ErrorDto errorDto = new ErrorDto(-2, errorMessage);
            return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
        }

        return new ResponseEntity<>(jsonResponse, HttpStatusCode.valueOf(200));
    }

}
