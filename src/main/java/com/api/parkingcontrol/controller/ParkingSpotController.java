package com.api.parkingcontrol.controller;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repository.ParkingSpotRepository;
import com.api.parkingcontrol.services.ParkingSpotService;

import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Api(value = "API REST ESTACIONAMENTO")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;
    private final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotController(ParkingSpotService parkingSpotService,
                                 ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotService = parkingSpotService;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){
        if(parkingSpotService.existsByPlacaDoCarro(parkingSpotDto.getPlacaDoCarro())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Placa do carro já existe!");
        }
        if (parkingSpotService.existsByNumeroDaVaga(parkingSpotDto.getNumeroDaVaga())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Numero da vaga já existe!");
        }
        if(parkingSpotService.existsByApartamentoAndBloco(parkingSpotDto.getApartamento(), parkingSpotDto.getBloco())){
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: Apartamento e bloco já existe!");
        }
       var parkingSpotModel  = new ParkingSpotModel();
       BeanUtils.copyProperties(parkingSpotDto,parkingSpotModel);
       parkingSpotModel.setDataDeRegistro(LocalDateTime.now(ZoneId.of("UTC")));
       return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(@PageableDefault(page = 0, size = 10,sort = "id", direction = Sort.Direction.ASC)Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if (!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Estacionamento deletado com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") UUID id,@RequestBody @Valid ParkingSpotDto parkingSpotDto){

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estacionamento não encontrado");
        }
        var parkingSpotModel = parkingSpotModelOptional.get();
        parkingSpotModel.setNumeroDaVaga(parkingSpotDto.getNumeroDaVaga());
        parkingSpotModel.setPlacaDoCarro(parkingSpotDto.getPlacaDoCarro());
        parkingSpotModel.setMarcaDoCarro(parkingSpotDto.getMarcaDoCarro());
        parkingSpotModel.setModeloDoCarro(parkingSpotDto.getModeloDoCarro());
        parkingSpotModel.setCorDoCarro(parkingSpotDto.getCorDoCarro());
        parkingSpotModel.setNomeDoResposavel(parkingSpotDto.getNomeDoResposavel());
        parkingSpotModel.setApartamento(parkingSpotDto.getApartamento());
        parkingSpotModel.setBloco(parkingSpotDto.getBloco());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }


}
