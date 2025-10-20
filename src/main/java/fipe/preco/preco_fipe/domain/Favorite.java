package fipe.preco.preco_fipe.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(nullable = false)
    private VehicleData vehicleData;
}
