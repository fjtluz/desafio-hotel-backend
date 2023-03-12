package com.senior.desafiohotel.dto;

public class MensagemDTO {

    public static String obrigatorio(String parametro) {
        return "Parâmetro '" + parametro + "' não foi encontrado, mesmo sendo obrigatório!";
    }
}
