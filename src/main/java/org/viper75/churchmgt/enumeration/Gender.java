package org.viper75.churchmgt.enumeration;

public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    UNDISCLOSED("Undisclosed");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String Gender(){
        return gender;
    }

    @Override
    public String toString(){
        return gender;
    }
}
