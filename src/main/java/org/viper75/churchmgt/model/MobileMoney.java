package org.viper75.churchmgt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.viper75.churchmgt.enumeration.AccountStatus;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MobileMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;
    private String provider;
    private int merchantCode;
    private AccountStatus accountStatus;

    @Builder
    public MobileMoney(String provider, int merchantCode){
        this.provider = provider;
        this.merchantCode = merchantCode;
        this.accountStatus = AccountStatus.ACTIVE;
    }
}
