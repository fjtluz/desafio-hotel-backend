package com.senior.desafiohotel.service;

import com.senior.desafiohotel.dto.CheckInDTO;
import com.senior.desafiohotel.dto.HospedeDTO;
import com.senior.desafiohotel.dto.RespostaDTO;
import com.senior.desafiohotel.entity.CheckIn;
import com.senior.desafiohotel.entity.CheckInId;
import com.senior.desafiohotel.entity.Hospede;
import com.senior.desafiohotel.repository.CheckInRepository;
import com.senior.desafiohotel.repository.HospedeRepository;
import jakarta.persistence.Tuple;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckInService {



    private final CheckInRepository checkInRepository;
    private final HospedeRepository hospedeRepository;

    public CheckInService(CheckInRepository checkInRepository, HospedeRepository hospedeRepository) {
        this.checkInRepository = checkInRepository;
        this.hospedeRepository = hospedeRepository;
    }


    public ResponseEntity<RespostaDTO<CheckIn>> cadastraNovoCheckIn(CheckInDTO checkIn) {
        CheckInId id = new CheckInId(checkIn.getHospede().getDocumento(), checkIn.getDataEntrada(), checkIn.getDataSaida());

        if (this.checkInRepository.findById(id).isPresent()) {
            return ResponseEntity.status(304).body(new RespostaDTO<>("Check in já cadastrada, favor informar outra!"));
        }

        HospedeDTO hospedeDTO = checkIn.getHospede();

        if (this.hospedeRepository.findById(hospedeDTO.getDocumento()).isEmpty()) {
            Hospede hospede = new Hospede(hospedeDTO.getDocumento(), hospedeDTO.getNome(), hospedeDTO.getTelefone());

            this.hospedeRepository.save(hospede);
        }

        CheckIn checkInEntity = new CheckIn(id, checkIn.isAdicionalVeiculo());

        return ResponseEntity.ok(new RespostaDTO<>(this.checkInRepository.save(checkInEntity)));
    }


}
