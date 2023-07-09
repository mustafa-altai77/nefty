package org.sudadev.nefty.models;

public class EmailConfirmationInputModel {
    private String email;
    private String code;

    public EmailConfirmationInputModel(String email, String code) {
        this.email = email;
        this.code = code;
    }
}

