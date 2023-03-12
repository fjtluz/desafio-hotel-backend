package com.senior.desafiohotel.controller;

import com.senior.desafiohotel.dto.HospedeDTO;
import com.senior.desafiohotel.dto.MensagemDTO;
import com.senior.desafiohotel.dto.RespostaDTO;
import com.senior.desafiohotel.service.HospedeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/hospede")
public class HospedeController {

    private final HospedeService hospedeService;

    public HospedeController(HospedeService hospedeService) {
        this.hospedeService = hospedeService;
    }

    @GetMapping()
    public ResponseEntity<RespostaDTO<List<HospedeDTO>>> buscaHospede(@RequestParam(required = false) String documento,
                                                                      @RequestParam(required = false) String nome,
                                                                      @RequestParam(required = false) String telefone) {
        if (documento == null && nome == null && telefone == null) {
            return ResponseEntity.status(200).headers(this.headers()).body(new RespostaDTO<>(this.hospedeService.retornaTodoHospede()));
        }

        List<HospedeDTO> possiveisHospedes = this.hospedeService.buscaHospede(new HospedeDTO(nome, documento, telefone));

        if (possiveisHospedes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RespostaDTO<>("Não foi encontrado nenhum hospede"));
        } else {
            return ResponseEntity.status(200).headers(this.headers()).body(new RespostaDTO<>(possiveisHospedes));
        }
    }

    @PostMapping()
    public ResponseEntity<RespostaDTO<HospedeDTO>> insereHospede(@RequestBody HospedeDTO hospede) {
        if (hospede.getDocumento() == null) {
            return ResponseEntity.badRequest().body(new RespostaDTO<>(MensagemDTO.obrigatorio("documento")));
        }
        RespostaDTO<HospedeDTO> retorno = this.hospedeService.insereHospede(hospede);

        if (retorno.getRetorno() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(retorno);
        }

        return ResponseEntity.ok(retorno);
    }

    @PutMapping()
    public ResponseEntity<RespostaDTO<HospedeDTO>> atualizaHospede(@RequestBody HospedeDTO hospedeDTO) {
        if (hospedeDTO.getDocumento() == null) {
            return ResponseEntity.badRequest().body(new RespostaDTO<>("Parâmetro 'documento' não foi encontrado, mesmo sendo obrigatório!"));
        }
        RespostaDTO<HospedeDTO> retorno = this.hospedeService.atualizaHospede(hospedeDTO);

        if (retorno.getRetorno() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(retorno);
        }

        return ResponseEntity.ok(retorno);
    }

    @DeleteMapping()
    public ResponseEntity<RespostaDTO<HospedeDTO>> deletaHospede(@RequestParam String documento) {
        if (documento == null) {
            return ResponseEntity.badRequest().body(new RespostaDTO<>(MensagemDTO.obrigatorio("documento")));
        }

        RespostaDTO<HospedeDTO> retorno = this.hospedeService.deletaHospede(documento);

        if (retorno.getRetorno() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(retorno);
        }

        return ResponseEntity.ok(retorno);
    }

    @GetMapping("/presentes")
    public ResponseEntity<RespostaDTO<List<HospedeDTO>>> buscaHospedesPresentes() {
        return ResponseEntity.ok(new RespostaDTO<>(this.hospedeService.retornaHospedesPresentes()));
    }

    @GetMapping("/ausentes")
    public ResponseEntity<RespostaDTO<List<HospedeDTO>>> buscaHospedesAusentes() {
        return ResponseEntity.ok(new RespostaDTO<>(this.hospedeService.retornaHospedesAusentes()));
    }

    public HttpHeaders headers() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        responseHeaders.set(HttpHeaders.CONTENT_TYPE, "application/json");
        return responseHeaders;
    }
}
