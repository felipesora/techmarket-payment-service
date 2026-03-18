package br.com.techmarket_payment_service.service;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoCreateDTO;
import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.mapper.PagamentoMapper;
import br.com.techmarket_payment_service.model.Pagamento;
import br.com.techmarket_payment_service.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Page<PagamentoResponseDTO> obterTodosPagamentos(Pageable paginacao) {
        return pagamentoRepository
                .findAll(paginacao)
                .map(PagamentoMapper::converterParaResponseDTO);
    }

    public PagamentoResponseDTO buscarPagamentoPorId(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento com id: " + id + " não encontrado"));

        return PagamentoMapper.converterParaResponseDTO(pagamento);
    }

    public PagamentoResponseDTO buscarPagamentoPorIdPedido(Long idPedido) {
        Pagamento pagamento = pagamentoRepository.findByIdPedido(idPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento com o IdPedido: " + idPedido + " não encontrado"));

        return PagamentoMapper.converterParaResponseDTO(pagamento);
    }

    @Transactional
    public PagamentoResponseDTO cadastrarPagamento(PagamentoCreateDTO pagamentoDTO) {
        Pagamento pagamento = PagamentoMapper.converterCreateDTOParaEntity(pagamentoDTO);
        pagamentoRepository.save(pagamento);
        return PagamentoMapper.converterParaResponseDTO(pagamento);
    }
}
