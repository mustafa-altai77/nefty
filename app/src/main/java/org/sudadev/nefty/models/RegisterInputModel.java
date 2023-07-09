package org.sudadev.nefty.models;

public class RegisterInputModel {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String idNumber;

    public RegisterInputModel(String fullName, String email, String mobile, String password, String identityNumber) {
        this.name = fullName;
        this.email = email;
        this.phone = mobile;
        this.password = password;
        this.idNumber = identityNumber;
    }
}
