package com.senior.desafiohotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "checkin")
public class CheckIn {

    @EmbeddedId
    CheckInId checkInId;

    @Column(name = "adicionalveiculo")
    boolean adicionalVeiculo;
}
