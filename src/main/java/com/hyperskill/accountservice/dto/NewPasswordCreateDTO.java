package com.hyperskill.accountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewPasswordCreateDTO {
    @JsonProperty("new_password")
    private String newPassword;

    public NewPasswordCreateDTO(){}

    public NewPasswordCreateDTO(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
