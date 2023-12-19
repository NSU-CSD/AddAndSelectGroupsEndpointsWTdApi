package com.krpo.krpo.controllers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krpo.krpo.controllers.helpers.InfoHelper;
import com.krpo.krpo.dtos.ChatInfoDTO;
import dev.voroby.springframework.telegram.client.TdApi;
import dev.voroby.springframework.telegram.client.TelegramClient;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class InfoController {

    private final TelegramClient telegramClient;
    private final InfoHelper infoHelper;

    public InfoController(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
        infoHelper = new InfoHelper(telegramClient);
    }

    @GetMapping("/getMe")
    public TdApi.User getMe() {
        return telegramClient.sendSync(new TdApi.GetMe());
    }

    @GetMapping("/chatTitles")
    public List<ChatInfoDTO> getMyChats() {
        TdApi.Chats chats = telegramClient.sendSync(new TdApi.GetChats(new TdApi.ChatListMain(), 100));
        return Arrays.stream(chats.chatIds)
                .mapToObj(chatId -> {
                    TdApi.Chat chat = telegramClient.sendSync(new TdApi.GetChat(chatId));
                    return new ChatInfoDTO(chatId, chat.title);
                }).toList();
    }

    @GetMapping("/sendHello")
    public void helloToYourself() {
        telegramClient.sendAsync(new TdApi.GetMe())
                .thenApply(user -> user.usernames.activeUsernames[0])
                .thenApply(username -> telegramClient.sendAsync(new TdApi.SearchChats(username, 1)))
                .thenCompose(chatsFuture ->
                        chatsFuture.thenApply(chats -> chats.chatIds[0]))
                .thenApply(chatId -> telegramClient.sendAsync(sendMessageQuery(chatId)));
    }

    static class HistoryRequestBody implements Serializable {
        @NonNull
        public long chatId;
        @NonNull
        public long number;
    }

    @GetMapping("/history")
    public String getHistory(@RequestBody HistoryRequestBody body) throws JsonProcessingException {
        List<InfoHelper.MessageInfo> result = infoHelper.getMessageInfoByChat(body.number, body.chatId);
        return InfoHelper.toJSON(result);
    }

    static class HistoryByUserRequestBody implements Serializable {
        @NonNull public long chatId;
        @NonNull public long number; // max number of messages
        @NonNull public String username; // containing @
    }

    @GetMapping("/historyByUser")
    public String getHistoryByUser(@RequestBody HistoryByUserRequestBody body) throws JsonProcessingException {
        ArrayList<InfoHelper.MessageInfo> messageInfos = infoHelper.getMessageInfoByChat(body.number, body.chatId);
        List<InfoHelper.MessageInfo> filtered = messageInfos.stream().filter(m -> m.user_id.equalsIgnoreCase(body.username)).toList();
        return InfoHelper.toJSON(filtered);
    }

    private TdApi.SendMessage sendMessageQuery(Long chatId) {
        var content = new TdApi.InputMessageText();
        var formattedText = new TdApi.FormattedText();
        formattedText.text = "Hello!";
        content.text = formattedText;
        return new TdApi.SendMessage(chatId, 0, null, null, null, content);
    }

}
