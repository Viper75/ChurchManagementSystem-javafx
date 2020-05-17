package org.viper75.churchmgt.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberDTO {
    private long memberId;
    private String memberFname;
    private String memberLname;
    private String memberMiddle;
    private String memberPrefix;
    private String memberSuffix;
    private String memberRelation;
    private String memberSex;
    private String memberHomePhone;
    private String memberCellPhone;
    private String memberAddress;
    private LocalDate memberDOB;
    private String memberMaritalStatus;
    private String memberEMail;
    private String memberType;
}
