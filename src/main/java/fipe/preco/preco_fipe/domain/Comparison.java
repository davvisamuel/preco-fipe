package fipe.preco.preco_fipe.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comparison")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comparison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "comparison", fetch = FetchType.LAZY)
    private Set<Consultation> consultations = new HashSet<>();
}
