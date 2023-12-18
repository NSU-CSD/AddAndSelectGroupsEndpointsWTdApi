package com.krpo.krpo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.voroby.springframework.telegram.client.TdApi;
import dev.voroby.springframework.telegram.client.TelegramClient;
import com.krpo.krpo.dtos.AddGroupDto;
import com.krpo.krpo.dtos.AddGroupResponseDto;
import dev.voroby.springframework.telegram.exception.TelegramClientTdApiException;
import com.krpo.krpo.dtos.ErrorDto;
import com.krpo.krpo.entites.UserGroup;
import com.krpo.krpo.mappers.repositories.UserGroupRepository;
import com.krpo.krpo.services.UserGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AddGroupController {

    private final UserGroupService userGroupService;
    private final UserGroupRepository userGroupRepository;
    private final TelegramClient telegramClient;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @PostMapping(path = "/addGroup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addGroup(@RequestBody @Valid AddGroupDto addGroupDto) {
        int telegramBoilerplatePlatformId = 1; //change that later when platform id is specified
        if (addGroupDto.getPlatform_id() == telegramBoilerplatePlatformId) {
            Long chat_id = addGroupDto.getChat_id();
            try {
                TdApi.Chat chat = telegramClient.sendSync(new TdApi.GetChat(chat_id));
                if (userGroupService.existsByLoginAndGroupId(addGroupDto.getUser_login(), chat_id)) {
                    String errorMessage = "InternalRequestError: login-group pair already added";
                    ErrorDto errorDto = new ErrorDto(-1, errorMessage);
                    return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
                } else {
                    userGroupService.saveUserGroup(new UserGroup(chat_id, chat.title, addGroupDto.getUser_login()));
                    AddGroupResponseDto addGroupResponseDto = new AddGroupResponseDto();
                    String jsonResponse = "";
                    try {
                        jsonResponse = objectWriter.writeValueAsString(addGroupResponseDto);
                    } catch (JsonProcessingException e) {
                        System.err.println(e.getMessage());

                        String errorMessage = "InternalRequestError: internal json mapping error: " + e.getMessage();
                        ErrorDto errorDto = new ErrorDto(-2, errorMessage);
                        return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
                    }
                    return new ResponseEntity<>(jsonResponse, HttpStatusCode.valueOf(200));
                }
            } catch (TelegramClientTdApiException e) {
                System.err.println(e.getMessage());

                String errorMessage = "InternalRequestError: bad group_id: " + e.getMessage();
                ErrorDto errorDto = new ErrorDto(-3, errorMessage);
                return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
            }
        } else {
            String errorMessage = "InternalRequestError: unsupported platform";
            ErrorDto errorDto = new ErrorDto(-4, errorMessage);
            return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
        }
    }

}
