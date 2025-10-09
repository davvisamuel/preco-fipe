package fipe.preco.preco_fipe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "fuel")
public class Fuel {

    @Id
    private Integer id;

    @Column(name = "fuel", nullable = false)
    private String fuel;

    @Column(name = "fuel_acronym", nullable = false)
    private String fuelAcronym;
}
