package br.com.fiap.api.repository;

import br.com.fiap.api.model.Filial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilialRepository  extends JpaRepository<Filial, Long> {

    Page<Filial> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}

