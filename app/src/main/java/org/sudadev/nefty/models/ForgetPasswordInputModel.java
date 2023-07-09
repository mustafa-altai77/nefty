package org.sudadev.nefty.models;

public class ForgetPasswordInputModel {
    private String idNumber;
    private String code;
    private String phone;
    private String password;

    public ForgetPasswordInputModel(String mobile, String idNumber) {
        this.phone = mobile;
        this.idNumber = idNumber;
    }

    public ForgetPasswordInputModel(String mobile, String idNumber, String code, String password) {
        this.phone = mobile;
        this.idNumber = idNumber;
        this.code = code;
        this.password = password;
    }
}
