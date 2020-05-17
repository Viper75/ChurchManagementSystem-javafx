package org.viper75.churchmgt.enumeration;

public enum AccountStatus {
    ACTIVE("Active"),
    DEACTIVATED("Deactivated");

    private String accountStatus;

    AccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }


    @Override
    public String toString() {
        return accountStatus;
    }
}
