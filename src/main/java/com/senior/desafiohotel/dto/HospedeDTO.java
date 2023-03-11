package com.senior.desafiohotel.dto;

import com.senior.desafiohotel.entity.Hospede;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospedeDTO {
    private String nome;
    private String documento;
    private String telefone;

    private Double valorTotal;
    private Double valorUltimaHospedagem;

    public HospedeDTO(String nome, String documento, String telefone) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
    }

    public static HospedeDTO transforma(Hospede hospede) {
        return new HospedeDTO(hospede.getNome(), hospede.getDocumento(), hospede.getTelefone());
    }
}
