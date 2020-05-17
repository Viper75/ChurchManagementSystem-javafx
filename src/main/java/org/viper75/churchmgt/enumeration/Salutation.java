package org.viper75.churchmgt.enumeration;

public enum Salutation {
    MR("Mr."),
    MRS("Mrs."),
    MS("Ms."),
    PAS("Pas."),
    DR("Dr.");

    private String salutation;

    Salutation(String salutation) {
        this.salutation = salutation;
    }

    public String Salutation(){
        return salutation;
    }

    @Override
    public String toString(){
        return salutation;
    }
}
