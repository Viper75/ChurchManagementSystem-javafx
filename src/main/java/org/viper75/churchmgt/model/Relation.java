package org.viper75.churchmgt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int relationId;

    @NaturalId
    @Column(nullable = false, length = 20)
    private String relation;

    @Override
    public String toString() {
        return relation;
    }
}
