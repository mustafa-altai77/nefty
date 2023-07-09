package org.sudadev.nefty.models;

public class ChangePasswordInputModel {
    private String oldPassword;
    private String newPassword;

    public ChangePasswordInputModel(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
