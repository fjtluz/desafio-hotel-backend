package com.senior.desafiohotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInId implements Serializable {

    @Column(name = "documento")
    String documento;

    @Column(name = "dataentrada")
    LocalDateTime dataEntrada;

    @Column(name = "datasaida")
    LocalDateTime dataSaida;

}
