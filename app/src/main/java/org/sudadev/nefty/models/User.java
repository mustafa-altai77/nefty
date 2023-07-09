package org.sudadev.nefty.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String fullName;
    private String email;
    private String mobile;
    private String userName;
    private String fireBaseToken;
    private String fireBaseTokenOS;
    private String imagePath;
    private String companyName;
    private String identityNumber;
    private String commercialRegister;
    private String createdOn;
    private String createdOnString;
    private String emailConfirmationToken;
    private String phoneConfirmationToken;
    private String resetPasswordToken;
    private String assignedToUserId;
    private String assignedToReviewerUserId;
    private int accountStatus;
    private int accountType;
    private boolean isMobileConfirmed;

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserName() {
        return userName;
    }

    public String getFireBaseToken() {
        return fireBaseToken;
    }

    public String getFireBaseTokenOS() {
        return fireBaseTokenOS;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getCommercialRegister() {
        return commercialRegister;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getCreatedOnString() {
        return createdOnString;
    }

    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public String getPhoneConfirmationToken() {
        return phoneConfirmationToken;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public int getAccountType() {
        return accountType;
    }

    public boolean isMobileConfirmed() {
        return isMobileConfirmed;
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public String getAssignedToReviewerUserId() {
        return assignedToReviewerUserId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.email);
        dest.writeString(this.mobile);
        dest.writeString(this.userName);
        dest.writeString(this.fireBaseToken);
        dest.writeString(this.fireBaseTokenOS);
        dest.writeString(this.imagePath);
        dest.writeString(this.companyName);
        dest.writeString(this.identityNumber);
        dest.writeString(this.commercialRegister);
        dest.writeString(this.createdOn);
        dest.writeString(this.createdOnString);
        dest.writeString(this.emailConfirmationToken);
        dest.writeString(this.phoneConfirmationToken);
        dest.writeString(this.resetPasswordToken);
        dest.writeInt(this.accountStatus);
        dest.writeInt(this.accountType);
        dest.writeByte(this.isMobileConfirmed ? (byte) 1 : (byte) 0);
        dest.writeString(this.assignedToUserId);
        dest.writeString(this.assignedToReviewerUserId);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.fullName = in.readString();
        this.email = in.readString();
        this.mobile = in.readString();
        this.userName = in.readString();
        this.fireBaseToken = in.readString();
        this.fireBaseTokenOS = in.readString();
        this.imagePath = in.readString();
        this.companyName = in.readString();
        this.identityNumber = in.readString();
        this.commercialRegister = in.readString();
        this.createdOn = in.readString();
        this.createdOnString = in.readString();
        this.emailConfirmationToken = in.readString();
        this.phoneConfirmationToken = in.readString();
        this.resetPasswordToken = in.readString();
        this.accountStatus = in.readInt();
        this.accountType = in.readInt();
        this.isMobileConfirmed = in.readByte() != 0;
        this.assignedToUserId = in.readString();
        this.assignedToReviewerUserId = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}


