package com.krpo.krpo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.krpo.krpo.dtos.ErrorDto;
import com.krpo.krpo.dtos.GroupDto;
import com.krpo.krpo.dtos.SelectGroupsDto;
import com.krpo.krpo.dtos.SelectGroupsResponseDto;
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
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SelectGroupsController {

    private final UserGroupService userGroupService;
    private final UserGroupRepository userGroupRepository;
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @PostMapping(path = "/selectGroups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> selectGroups(@RequestBody @Valid SelectGroupsDto selectGroupsDto) {
        //get list of groups for specified login
        String login = selectGroupsDto.getUser_login();
        List<UserGroup> userGroupList = userGroupRepository.findGroupsByLogin(login);
        if (userGroupList.isEmpty()) {
            String errorMessage = "InternalRequestError: no groups found for specified login";
            ErrorDto errorDto = new ErrorDto(-1, errorMessage);
            return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
        }

        List<GroupDto> groupDtoList = new ArrayList<>();
        for (UserGroup ug : userGroupList) {
            groupDtoList.add(new GroupDto(ug.getGroupId(), ug.getGroupName(), ug.getLogin()));
        }
        String formatVersionBoilerplate = "format_version=1";
        SelectGroupsResponseDto selectGroupsResponseDto = new SelectGroupsResponseDto(formatVersionBoilerplate, login, groupDtoList);

        String jsonResponse = "";
        try {
            jsonResponse = objectWriter.writeValueAsString(selectGroupsResponseDto);
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());

            String errorMessage = "InternalRequestError: internal json mapping error: " + e.getMessage();
            ErrorDto errorDto = new ErrorDto(-2, errorMessage);
            return new ResponseEntity<>(errorDto.toJSONString(), HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<>(jsonResponse, HttpStatusCode.valueOf(200));
    }

}

