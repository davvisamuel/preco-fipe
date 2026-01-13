package fipe.preco.preco_fipe.repository;

import fipe.preco.preco_fipe.domain.VehicleData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleDataRepository extends JpaRepository<VehicleData, Integer> {

    Optional<VehicleData> findByCodeFipeAndModelYear(String codeFipe, String modelYear);

    String model(String model);
}
