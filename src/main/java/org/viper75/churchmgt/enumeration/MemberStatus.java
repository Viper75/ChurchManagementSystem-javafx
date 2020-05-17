package org.viper75.churchmgt.enumeration;

public enum MemberStatus {
    ACTIVE("Active"),
    IN_ACTIVE("Inactive");

    private String status;

    MemberStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return status;
    }
}
