package com.senior.desafiohotel.repository;

import com.senior.desafiohotel.entity.CheckIn;
import com.senior.desafiohotel.entity.CheckInId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, CheckInId> {

    List<CheckIn> findAllByCheckInId_Documento(String documento);

    @Query("SELECT ci.checkInId.documento FROM CheckIn ci WHERE CURRENT_TIMESTAMP BETWEEN ci.checkInId.dataEntrada AND ci.checkInId.dataSaida")
    List<String> findAllPresent();
}
