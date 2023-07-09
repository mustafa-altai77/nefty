package org.sudadev.nefty.models;

public class EditProfileModel {
    private String fullName;
    private String mobile;
    private String email;
    private String imagePath;
    private String companyName;
    private String identityNumber;
    private String commercialRegister;

    public EditProfileModel(String fullName, String mobile, String email, String imagePath, String companyName,
                            String identityNumber, String commercialRegister) {
        this.fullName = fullName;
        this.mobile = mobile;
        this.email = email;
        this.imagePath = imagePath;
        this.companyName = companyName;
        this.identityNumber = identityNumber;
        this.commercialRegister = commercialRegister;
    }
}