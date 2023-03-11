package com.senior.desafiohotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespostaDTO<T> {

    private T retorno;
    private String mensagem;

    public RespostaDTO(T retorno, String mensagem) {
        this.retorno = retorno;
        this.mensagem = mensagem;
    }

    public RespostaDTO(T retorno) {
        this.retorno = retorno;
        this.mensagem = "OK";
    }

    public RespostaDTO(String mensagem) {
        this.retorno = null;
        this.mensagem = mensagem;
    }
}
