package ru.glitchless.telegrambridge.telegramapi.model;

import com.google.gson.annotations.SerializedName;

public class ChatObject {
    @SerializedName("id")
    private Long id = 0L;
    @SerializedName("type")
    private String type = "";
    @SerializedName("title")
    private String title = "";
    @SerializedName("username")
    private String username = "";
    @SerializedName("first_name")
    private String firstName = "";
    @SerializedName("last_name")
    private String lastName = "";
    @SerializedName("all_members_are_administrators")
    private Boolean allAdmin = false;
    @SerializedName("description")
    private String description = "";
    @SerializedName("invite_link")
    private String inviteLink = "";
    @SerializedName("pinned_message")
    private MessageObject pinnedMessage = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getAllAdmin() {
        return allAdmin;
    }

    public void setAllAdmin(Boolean allAdmin) {
        this.allAdmin = allAdmin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInviteLink() {
        return inviteLink;
    }

    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }

    public MessageObject getPinnedMessage() {
        return pinnedMessage;
    }

    public void setPinnedMessage(MessageObject pinnedMessage) {
        this.pinnedMessage = pinnedMessage;
    }
}
