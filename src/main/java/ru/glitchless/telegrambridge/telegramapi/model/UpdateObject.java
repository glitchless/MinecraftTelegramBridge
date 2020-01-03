package ru.glitchless.telegrambridge.telegramapi.model;

import com.google.gson.annotations.SerializedName;

public class UpdateObject {
    @SerializedName("update_id")
    private Long updateId = 0L;
    @SerializedName("message")
    private MessageObject message = null;
    @SerializedName("edited_message")
    private MessageObject editedMessage = null;

    public Long getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

    public MessageObject getMessage() {
        return message;
    }

    public void setMessage(MessageObject message) {
        this.message = message;
    }

    public MessageObject getEditedMessage() {
        return editedMessage;
    }

    public void setEditedMessage(MessageObject editedMessage) {
        this.editedMessage = editedMessage;
    }
}
