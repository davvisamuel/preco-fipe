package fipe.preco.preco_fipe.service;

import fipe.preco.preco_fipe.domain.Fuel;
import fipe.preco.preco_fipe.exception.NotFoundException;
import fipe.preco.preco_fipe.repository.FuelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuelService {
    private final FuelRepository fuelRepository;

    public Fuel findByFuelAcronym(String fuelAcronym) {
        return fuelRepository.findByFuelAcronym(fuelAcronym)
                .orElseThrow(() -> new NotFoundException("Fuel not found"));
    }
}
