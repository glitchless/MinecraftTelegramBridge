package ru.glitchless.telegrambridge.core.telegramapi.model;

import com.google.gson.annotations.SerializedName;

public class MessageObject {
    @SerializedName("message_id")
    private Long messageId = 0L;
    @SerializedName("from")
    private UserObject from = null;
    @SerializedName("date")
    private Long date = 0L;
    @SerializedName("chat")
    private ChatObject chat = null;
    @SerializedName("forward_from")
    private UserObject forwardFrom = null;
    private ChatObject forwardFromChat = null;
    private Long forwardFromMessageId = null;
    @SerializedName("forward_signature")
    private String forwardSignature = null;
    @SerializedName("forward_sender_name")
    private String forwardSenderName = null;
    @SerializedName("forward_date")
    private Long forwardDate = 0L;
    @SerializedName("reply_to_message")
    private MessageObject replyToMessage = null;
    @SerializedName("edit_date")
    private Long editDate = 0L;
    @SerializedName("media_group_id")
    private String mediaGroupId = "";
    @SerializedName("author_signature")
    private String authorSignature = "";
    @SerializedName("text")
    private String text = "";

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public UserObject getFrom() {
        return from;
    }

    public void setFrom(UserObject from) {
        this.from = from;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public ChatObject getChat() {
        return chat;
    }

    public void setChat(ChatObject chat) {
        this.chat = chat;
    }

    public UserObject getForwardFrom() {
        return forwardFrom;
    }

    public void setForwardFrom(UserObject forwardFrom) {
        this.forwardFrom = forwardFrom;
    }

    public ChatObject getForwardFromChat() {
        return forwardFromChat;
    }

    public void setForwardFromChat(ChatObject forwardFromChat) {
        this.forwardFromChat = forwardFromChat;
    }

    public Long getForwardFromMessageId() {
        return forwardFromMessageId;
    }

    public void setForwardFromMessageId(Long forwardFromMessageId) {
        this.forwardFromMessageId = forwardFromMessageId;
    }

    public String getForwardSignature() {
        return forwardSignature;
    }

    public void setForwardSignature(String forwardSignature) {
        this.forwardSignature = forwardSignature;
    }

    public String getForwardSenderName() {
        return forwardSenderName;
    }

    public void setForwardSenderName(String forwardSenderName) {
        this.forwardSenderName = forwardSenderName;
    }

    public Long getForwardDate() {
        return forwardDate;
    }

    public void setForwardDate(Long forwardDate) {
        this.forwardDate = forwardDate;
    }

    public MessageObject getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(MessageObject replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public Long getEditDate() {
        return editDate;
    }

    public void setEditDate(Long editDate) {
        this.editDate = editDate;
    }

    public String getMediaGroupId() {
        return mediaGroupId;
    }

    public void setMediaGroupId(String mediaGroupId) {
        this.mediaGroupId = mediaGroupId;
    }

    public String getAuthorSignature() {
        return authorSignature;
    }

    public void setAuthorSignature(String authorSignature) {
        this.authorSignature = authorSignature;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
