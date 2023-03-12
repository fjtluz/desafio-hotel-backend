package com.senior.desafiohotel.service;

import com.senior.desafiohotel.dto.CheckInDTO;
import com.senior.desafiohotel.dto.HospedeDTO;
import com.senior.desafiohotel.dto.MensagemDTO;
import com.senior.desafiohotel.dto.RespostaDTO;
import com.senior.desafiohotel.entity.CheckIn;
import com.senior.desafiohotel.entity.CheckInId;
import com.senior.desafiohotel.entity.Hospede;
import com.senior.desafiohotel.repository.CheckInRepository;
import com.senior.desafiohotel.repository.HospedeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CheckInService {



    private final CheckInRepository checkInRepository;
    private final HospedeRepository hospedeRepository;

    public CheckInService(CheckInRepository checkInRepository, HospedeRepository hospedeRepository) {
        this.checkInRepository = checkInRepository;
        this.hospedeRepository = hospedeRepository;
    }


    public ResponseEntity<RespostaDTO<CheckIn>> cadastraNovoCheckIn(CheckInDTO checkIn) {

        if (checkIn.getHospede().getDocumento() == null) {
            return ResponseEntity.badRequest().body(new RespostaDTO<>(MensagemDTO.obrigatorio("hospede.documento")));
        }

        CheckInId id = new CheckInId(checkIn.getHospede().getDocumento(), checkIn.getDataEntrada(), checkIn.getDataSaida());

        if (this.checkInRepository.findById(id).isPresent()) {
            return ResponseEntity.status(304).body(new RespostaDTO<>("Check in já cadastrada, favor informar outra!"));
        }

        HospedeDTO hospedeDTO = checkIn.getHospede();

        boolean novoHospede = this.hospedeRepository.findById(hospedeDTO.getDocumento()).isEmpty();

        if (novoHospede) {
            if (hospedeDTO.getTelefone() != null && hospedeDTO.getNome() != null) {
                Hospede hospede = new Hospede(hospedeDTO.getDocumento(), hospedeDTO.getNome(), hospedeDTO.getTelefone());
                this.hospedeRepository.save(hospede);
            } else {
                return ResponseEntity.badRequest()
                        .body(new RespostaDTO<>("Hospede com documento '" + hospedeDTO.getDocumento() + "' não encontrado. Favor informar 'hospede.nome' e 'hospede.telefone' para concluir o check in!"));
            }
        }

        CheckIn checkInEntity = new CheckIn(id, checkIn.isAdicionalVeiculo());

        return ResponseEntity.ok(new RespostaDTO<>(this.checkInRepository.save(checkInEntity)));
    }


}
