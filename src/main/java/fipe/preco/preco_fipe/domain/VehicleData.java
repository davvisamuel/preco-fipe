package fipe.preco.preco_fipe.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String codeFipe;

    @OneToOne(optional = false)
    private Fuel fuel;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String modelYear;

    private String vehicleType;
}
