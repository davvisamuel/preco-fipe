package fipe.preco.preco_fipe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    private User user;
    @OneToOne(optional = false)
    private VehicleQuery vehicleQuery;
}
