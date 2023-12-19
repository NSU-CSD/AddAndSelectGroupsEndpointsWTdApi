package com.krpo.krpo.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.krpo.krpo.MLstub.MLRequestDTO;
import com.krpo.krpo.controllers.helpers.InfoHelper;
import com.krpo.krpo.dtos.AnalyseToxicityDto;
import com.krpo.krpo.dtos.ErrorDto;
import com.krpo.krpo.entites.UserGroup;
import com.krpo.krpo.mappers.repositories.UserGroupRepository;
import com.krpo.krpo.services.UserGroupService;
import dev.voroby.springframework.telegram.client.TdApi;
import dev.voroby.springframework.telegram.client.TelegramClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/analyse", produces = MediaType.APPLICATION_JSON_VALUE)
public class AnalysisController {

    private final TelegramClient telegramClient;
    private final UserGroupService userGroupService;
    private final UserGroupRepository userGroupRepository;
    private final InfoHelper infoHelper;

    public AnalysisController(TelegramClient telegramClient,
                              UserGroupService userGroupService,
                              UserGroupRepository userGroupRepository) {
        this.telegramClient = telegramClient;
        this.userGroupRepository = userGroupRepository;
        this.userGroupService = userGroupService;

        this.infoHelper = new InfoHelper(telegramClient);
    }

    public void checkUsernames(List<String> usernames) {
        if (1 == usernames.size()) {
            //"all" case
            return;
        }
        //cannot check if user is valid by their username
        return;
    }

    public List<InfoHelper.MessageInfo> getHistoryByUsers(List<String> usernames, long chatId, long numberOfMessages) {
        if (1 == usernames.size()) {
            if (usernames.get(0).equals("all")) {
                List<InfoHelper.MessageInfo> messageInfos = infoHelper.getMessageInfoByChat(numberOfMessages, chatId);
                return messageInfos;
            }
        }
        List<InfoHelper.MessageInfo> messageInfos = infoHelper.getMessageInfoByChat(numberOfMessages, chatId);
        List<InfoHelper.MessageInfo> filtered = messageInfos.stream().filter(m -> usernames.contains(m.user_id)).toList();
        return filtered;
    }

    @PostMapping(value = "/toxicity", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> analyseToxicity(@RequestBody AnalyseToxicityDto body) {
        //currently only support telegram
        //chatId supposedly is valid

        //check usernames
        checkUsernames(body.userNames);

        //check if group (chat) exists in DB
        Optional<UserGroup> userGroupO = userGroupRepository.findByGroupId(body.chatId);
        if (userGroupO.isEmpty()) {
            String errorMessage = "InternalRequestError: group not found in database";
            ErrorDto errorDto = new ErrorDto(-1, errorMessage);
            return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
        }

        //get list of users' messages
        List<InfoHelper.MessageInfo> messageInfos = getHistoryByUsers(body.userNames, body.chatId, body.number);

        //make a call to api
        String uri = "http://localhost:8080/ML/analyse";
        RestTemplate restTemplate = new RestTemplate();
        MLRequestDTO requestDTO = new MLRequestDTO();
        requestDTO.setMessageInfoList(messageInfos);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, requestDTO, String.class);

        return new ResponseEntity<>(result.getBody(), HttpStatusCode.valueOf(200));
    }

}
