package ru.glitchless.telegrambridge.core.telegramapi.model;

import com.google.gson.annotations.SerializedName;

public class UserObject {
    @SerializedName("id")
    private Long id = 0L;
    @SerializedName("is_bot")
    private Boolean isBot = false;
    @SerializedName("first_name")
    private String firstName = "";
    @SerializedName("last_name")
    private String lastName = "";
    @SerializedName("username")
    private String username = "";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getBot() {
        return isBot;
    }

    public void setBot(Boolean bot) {
        isBot = bot;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
