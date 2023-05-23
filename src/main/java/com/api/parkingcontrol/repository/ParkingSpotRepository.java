package com.api.parkingcontrol.repository;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpotModel, UUID> {

    boolean existsByPlacaDoCarro(String placaDoCarro);

    boolean existsByNumeroDaVaga(String numeroDaVaga);

    boolean existsByApartamentoAndBloco(String apartamento, String bloco);
}
