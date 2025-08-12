package fipe.preco.preco_fipe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VehicleQuery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String vehicleType;
    @Column(nullable = false)
    private String brandId;
    @Column(nullable = false)
    private String modelId;
    @Column(nullable = false)
    private String yearId;
}
