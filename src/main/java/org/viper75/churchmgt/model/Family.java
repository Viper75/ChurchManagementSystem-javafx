package org.viper75.churchmgt.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Family {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long familyId;

    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "family", fetch = FetchType.EAGER)
    private List<Member> familyMembers = new ArrayList<>();

}

