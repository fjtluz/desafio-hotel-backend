package com.senior.desafiohotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CheckInDTO {

    private HospedeDTO hospede;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private boolean adicionalVeiculo;

}
