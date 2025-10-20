package fipe.preco.preco_fipe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleType {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String typeName;

}
