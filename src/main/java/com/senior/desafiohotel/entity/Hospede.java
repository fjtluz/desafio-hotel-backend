package com.senior.desafiohotel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "hospede")
public class Hospede {

    @Id
    @Column(name = "documento")
    String documento;

    @Column(name = "nome")
    String nome;

    @Column(name = "telefone")
    String telefone;

}
