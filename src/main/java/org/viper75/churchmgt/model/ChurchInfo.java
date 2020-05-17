package org.viper75.churchmgt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.viper75.churchmgt.model.embeddable.Address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChurchInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int churchInfoId;
    private String churchName;
    private Address churchAddress;
    private String contactPerson;
    private String telephoneNumber;
    private String faxNumber;
    private String cellphoneNumber;
    private String emailAddress;
    private String logoPath;
    private String motto;
}
