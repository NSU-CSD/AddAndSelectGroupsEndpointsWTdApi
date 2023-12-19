package com.krpo.krpo.controllers.helpers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krpo.krpo.controllers.InfoController;
import dev.voroby.springframework.telegram.client.TdApi;
import dev.voroby.springframework.telegram.client.TelegramClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoHelper {

    private final TelegramClient telegramClient;

    public InfoHelper(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    private String getText(TdApi.Message m) {
        if (m.content instanceof TdApi.MessageText)
            return ((TdApi.MessageText) (m.content)).text.text;
        return "";
    }

    private String getUserName(long userId) {
        TdApi.User sender = telegramClient.sendSync(new TdApi.GetUser(userId));
        String senderReadable = "@%s".formatted(sender.usernames.activeUsernames[0]);
        return senderReadable;
    }

    private String getDate(long date) {
        Date time = new Date(date *1000);
        String pattern = "dd.MM.yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String formattedDate = simpleDateFormat.format(time);
        return formattedDate;
    }

    public static String toJSON(List<MessageInfo> result) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper.writeValueAsString(result);
    }

    @NotNull
    public ArrayList<MessageInfo> getMessageInfoByChat(long number, long chatId) {
        long from = 0;

        ArrayList<MessageInfo> result = new ArrayList<>();
        for (long i = 0; i < number; i++) {
            TdApi.Messages history = telegramClient.sendSync(new TdApi.GetChatHistory(
                    (long) chatId, from, 0, 1, false));


            for (TdApi.Message m: history.messages) {
                if (!(m.senderId instanceof TdApi.MessageSenderUser)) continue;
                long userId = ((TdApi.MessageSenderUser)(m.senderId)).userId;

                String formattedDate = getDate(m.date);
                String senderReadable = getUserName(userId);

                MessageInfo messageInfo = new MessageInfo(senderReadable, getText(m), formattedDate);
                result.add(messageInfo);
            }

            if (history.messages.length > 0) {
                from = history.messages[0].id;
            } else {
                break;
            }
        }
        return result;
    }

    public final static class MessageInfo implements Serializable {
        @NonNull
        public final String user_id; // not ID but username, containing '@'
        @NonNull
        public final String message;
        @NonNull
        public final String publication_time;

        public MessageInfo(String user_id, String message, String publication_time) {
            this.user_id = user_id;
            this.message = message;
            this.publication_time = publication_time;
        }
    }

}
