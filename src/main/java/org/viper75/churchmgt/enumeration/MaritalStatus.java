package org.viper75.churchmgt.enumeration;

public enum MaritalStatus {
    SINGLE("Single"),
    WIDOWED("Widowed"),
    MARRIED("Married"),
    DEVORCED("Devorced"),
    OTHER("Other");

    private String status;

    MaritalStatus(String status) {
        this.status = status;
    }

    public String MaritalStatus(){
        return status;
    }


    @Override
    public String toString() {
        return status;
    }
}
