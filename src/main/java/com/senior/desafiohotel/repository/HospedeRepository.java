package com.senior.desafiohotel.repository;

import com.senior.desafiohotel.entity.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospedeRepository extends JpaRepository<Hospede, String> {

    @Query("SELECT hos FROM Hospede hos WHERE UPPER(hos.nome) LIKE CONCAT('%', UPPER(:nome), '%') OR UPPER(hos.telefone) LIKE CONCAT('%', UPPER(:telefone), '%')")
    List<Hospede> searchAllByNomeLikeOrTelefoneLike(@Param("nome") String nome, @Param("telefone") String telefone);
}
