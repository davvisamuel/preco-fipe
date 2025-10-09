package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.Fuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuelRepository extends JpaRepository<Fuel, Integer> {

    Optional<Fuel> findByFuelAcronym(String fuelAcronym);
}
