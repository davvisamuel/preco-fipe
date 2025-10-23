package fipe.preco.preco_fipe.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "consultation")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "vehicle_data_id", nullable = false)
    private VehicleData vehicleData;

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String referenceMonth;

    @ManyToOne
    @JoinColumn(name = "comparison_id")
    private Comparison comparison;
}
