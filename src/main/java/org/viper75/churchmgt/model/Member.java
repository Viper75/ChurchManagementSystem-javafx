package org.viper75.churchmgt.model;

import lombok.*;
import org.viper75.churchmgt.enumeration.Gender;
import org.viper75.churchmgt.enumeration.MaritalStatus;
import org.viper75.churchmgt.enumeration.MemberStatus;
import org.viper75.churchmgt.enumeration.Salutation;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(nullable = false, length = 30)
    private String memberFname;

    @Column(nullable = false, length = 30)
    private String memberLname;

    @Column(length = 30)
    private String memberMiddle;

    @Enumerated(EnumType.STRING)
    private Salutation memberSalutaion;

    @Column(length = 10)
    private String memberSuffix;

    @ManyToOne
    @JoinColumn(name = "memberRelation")
    private Relation memberRelation;

    @Enumerated(EnumType.STRING)
    private Gender memberGender;

    @Column(length = 14)
    private String memberHomePhone;

    @Column(length = 14)
    private String memberCellPhone;

    private Address memberAddress;

    private LocalDate memberDOB;

    @Enumerated(EnumType.STRING)
    private MaritalStatus memberMaritalStatus;

    private String memberEMail;

    @ManyToOne
    @JoinColumn(name = "memberType")
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Column(nullable = false, length = 3)
    private String mainMember;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "family_members",
            joinColumns = @JoinColumn(name = "memberId"),
            inverseJoinColumns = @JoinColumn(name = "familyId")
    )
    private Family family;

    @Builder
    public Member(String fname, String lname, String mainMember, Gender gender, MaritalStatus maritalStatus, MemberType memberType ) {
        this.memberFname = fname;
        this.memberLname = lname;
        this.mainMember = mainMember;
        this.memberGender = gender;
        this.memberMaritalStatus = maritalStatus;
        this.memberType = memberType;
        this.memberStatus = MemberStatus.ACTIVE;
    }

}
