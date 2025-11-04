package br.com.fiap.api.service;

import br.com.fiap.api.dto.FilialRequest;
import br.com.fiap.api.dto.FilialResponse;
import br.com.fiap.api.mapper.FilialMapper;
import br.com.fiap.api.model.Filial;
import br.com.fiap.api.repository.FilialRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilialService {

    private final FilialRepository filialRepository;

    @Autowired
    public FilialService(FilialRepository filialRepository) { // Renamed constructor
        this.filialRepository = filialRepository;
    }

    @Transactional(readOnly = true)
    public Page<FilialResponse> findAll(Pageable pageable) {
        Page<Filial> filiais = filialRepository.findAll(pageable);
        return filiais.map(FilialMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public FilialResponse findById(Long id) {
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));
        return FilialMapper.toResponseDTO(filial);
    }

    @Transactional
    public FilialResponse create(FilialRequest filialRequestDTO) {
        Filial filial = FilialMapper.toEntity(filialRequestDTO);
        Filial savedFilial = filialRepository.save(filial);
        return FilialMapper.toResponseDTO(savedFilial);
    }


    @Transactional
    public FilialResponse update(Long id, FilialRequest filialRequestDTO) {
        Filial existingFilial = filialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada com ID: " + id));


        existingFilial.setNome(filialRequestDTO.nome());
        existingFilial.setPais(filialRequestDTO.pais());
        existingFilial.setLogradouro(filialRequestDTO.logradouro());

        Filial updatedFilial = filialRepository.save(existingFilial);
        return FilialMapper.toResponseDTO(updatedFilial);
    }


    @Transactional
    public void delete(Long id) {
        if (!filialRepository.existsById(id)) {
            throw new EntityNotFoundException("Filial não encontrada com ID: " + id);
        }
        filialRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<FilialResponse> findByNome(String nome, Pageable pageable) {
        Page<Filial> filiais = filialRepository.findByNomeContainingIgnoreCase(nome, pageable);
        return filiais.map(FilialMapper::toResponseDTO);
    }
}
