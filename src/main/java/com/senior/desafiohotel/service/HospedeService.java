package com.senior.desafiohotel.service;

import com.senior.desafiohotel.dto.HospedeDTO;
import com.senior.desafiohotel.dto.RespostaDTO;
import com.senior.desafiohotel.entity.CheckIn;
import com.senior.desafiohotel.entity.CheckInId;
import com.senior.desafiohotel.entity.Hospede;
import com.senior.desafiohotel.repository.CheckInRepository;
import com.senior.desafiohotel.repository.HospedeRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HospedeService {

    private final double VALOR_DIARIA_UTIL = 120.00;
    private final double VALOR_DIARA_NAO_UTIL = 150.00;
    private final double ACRESCIMO_UTIL = 15.00;
    private final double ACRESCIMO_NAO_UTIL = 20.00;

    private final HospedeRepository hospedeRepository;
    private final CheckInRepository checkInRepository;

    public HospedeService(HospedeRepository hospedeRepository, CheckInRepository checkInRepository) {
        this.hospedeRepository = hospedeRepository;
        this.checkInRepository = checkInRepository;
    }

    public List<HospedeDTO> buscaHospede(HospedeDTO hospedeDTO) {

        if (hospedeDTO.getDocumento() != null) {
            Optional<Hospede> optionalHospede = this.hospedeRepository.findById(hospedeDTO.getDocumento());

            if (optionalHospede.isPresent()) {
                hospedeDTO = HospedeDTO.transforma(optionalHospede.get());
                this.calculaValores(hospedeDTO);
                return List.of(hospedeDTO);
            }
        }

        List<Hospede> possiveisHospedes = this.hospedeRepository.searchAllByNomeLikeOrTelefoneLike(hospedeDTO.getNome(), hospedeDTO.getTelefone());

        return possiveisHospedes.stream().map(hospede -> {
            HospedeDTO dto = HospedeDTO.transforma(hospede);
            calculaValores(dto);
            return dto;
        }).toList();
    }

    public List<HospedeDTO> retornaTodoHospede() {
        List<Hospede> listHospedes = this.hospedeRepository.findAll();

        return listHospedes.stream().map(hospede -> {
            HospedeDTO dto = HospedeDTO.transforma(hospede);
            this.calculaValores(dto);
            return dto;
        }).toList();
    }

    public List<HospedeDTO> retornaHospedesPresentes() {
        return this.retornaTodoHospede()
                .stream()
                .filter(hospedeDTO -> this.checkInRepository.findAllPresent().contains(hospedeDTO.getDocumento()))
                .toList();
    }

    public List<HospedeDTO> retornaHospedesAusentes() {
        return this.retornaTodoHospede()
                .stream()
                .filter(hospedeDTO -> !this.checkInRepository.findAllPresent().contains(hospedeDTO.getDocumento()))
                .toList();
    }

    public RespostaDTO<HospedeDTO> insereHospede(HospedeDTO hospedeDTO) {
        Hospede hospede = new Hospede(hospedeDTO.getDocumento(), hospedeDTO.getNome(), hospedeDTO.getTelefone());

        if (this.hospedeRepository.findById(hospede.getDocumento()).isPresent()) {
            return new RespostaDTO<>("Hospede com documento '" + hospede.getDocumento() + "' já cadastrado!");
        }

        hospede = this.hospedeRepository.save(hospede);
        return new RespostaDTO<>(HospedeDTO.transforma(hospede));
    }

    public RespostaDTO<HospedeDTO> atualizaHospede(HospedeDTO hospedeDTO) {
        Hospede hospede = new Hospede(hospedeDTO.getDocumento(), hospedeDTO.getNome(), hospedeDTO.getTelefone());

        if (this.hospedeRepository.findById(hospede.getDocumento()).isEmpty()) {
            return new RespostaDTO<>("Hospede com documento '" + hospede.getDocumento() + "' não existe no sistema!");
        }

        hospede = this.hospedeRepository.save(hospede);
        hospedeDTO = HospedeDTO.transforma(hospede);
        this.calculaValores(hospedeDTO);
        return new RespostaDTO<>(hospedeDTO);
    }

    public RespostaDTO<HospedeDTO> deletaHospede(String documento) {
        Optional<Hospede> optionalHospede = this.hospedeRepository.findById(documento);

        if (optionalHospede.isEmpty()) {
            return new RespostaDTO<>("Hospede com documento '" + documento + "' não existe no sistema!");
        }

        Hospede hospede = optionalHospede.get();
        HospedeDTO hospedeDTO = HospedeDTO.transforma(hospede);
        this.calculaValores(hospedeDTO);

        List<CheckInId> checkInList = this.checkInRepository
                .findAllByCheckInId_Documento(hospede.getDocumento())
                .stream().map(CheckIn::getCheckInId).toList();

        this.checkInRepository.deleteAllById(checkInList);
        this.hospedeRepository.delete(hospede);

        return new RespostaDTO<>(hospedeDTO, "Usuário com documento '" + documento + "' deletado com sucesso!");
    }

    private void calculaValores(HospedeDTO hospede) {
        List<CheckIn> checkInList = this.checkInRepository.findAllByCheckInId_Documento(hospede.getDocumento());

        double valorTotal = 0.0;

        LocalDateTime dataUltimaHospedagem = LocalDateTime.MIN;
        double valorUltimaHospedagem = 0.0;

        for (CheckIn checkIn : checkInList) {

            double valorHospedagem = 0.0;

            LocalDateTime dataEntrada = checkIn.getCheckInId().getDataEntrada();
            LocalDateTime dataSaida = checkIn.getCheckInId().getDataSaida();

            while (dataEntrada.toLocalDate().isBefore(dataSaida.toLocalDate())) {

                boolean isUtil = dataEntrada.getDayOfWeek().getValue() < 6;

                valorHospedagem += isUtil ? this.VALOR_DIARIA_UTIL : this.VALOR_DIARA_NAO_UTIL;

                if (checkIn.isAdicionalVeiculo()) {
                    valorHospedagem += isUtil ? this.ACRESCIMO_UTIL : this.ACRESCIMO_NAO_UTIL;
                }

                dataEntrada = dataEntrada.plusDays(1);
            }

            LocalDateTime diariaExtra = LocalDateTime.of(dataEntrada.toLocalDate(), LocalTime.of(16, 30, 0));
            if (dataSaida.isAfter(diariaExtra)) {
                boolean isUtil = dataSaida.getDayOfWeek().getValue() < 6;

                valorHospedagem += isUtil ? this.VALOR_DIARIA_UTIL : this.VALOR_DIARA_NAO_UTIL;
            }

            if (dataSaida.isAfter(dataUltimaHospedagem)) {
                dataUltimaHospedagem = dataSaida;
                valorUltimaHospedagem = valorHospedagem;
            }

            valorTotal += valorHospedagem;
        }

        hospede.setValorUltimaHospedagem(valorUltimaHospedagem);
        hospede.setValorTotal(valorTotal);
    }
}
