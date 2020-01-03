package ru.glitchless.telegrambridge.telegramapi.model;

import com.google.gson.annotations.SerializedName;

public class TelegramAnswerObject<T> {
    @SerializedName("ok")
    private Boolean ok = false;
    @SerializedName("result")
    private T result = null;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
