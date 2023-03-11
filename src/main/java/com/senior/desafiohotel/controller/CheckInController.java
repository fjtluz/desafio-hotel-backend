package com.senior.desafiohotel.controller;

import com.senior.desafiohotel.dto.CheckInDTO;
import com.senior.desafiohotel.dto.HospedeDTO;
import com.senior.desafiohotel.dto.RespostaDTO;
import com.senior.desafiohotel.entity.CheckIn;
import com.senior.desafiohotel.entity.Hospede;
import com.senior.desafiohotel.exception.MissingParamsException;
import com.senior.desafiohotel.service.CheckInService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/check-in")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping()
    public ResponseEntity<RespostaDTO<CheckIn>> cadastraNovoCheckIn(@RequestBody CheckInDTO checkIn) {
        return this.checkInService.cadastraNovoCheckIn(checkIn);
    }




}
