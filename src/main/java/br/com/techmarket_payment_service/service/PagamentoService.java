package br.com.techmarket_payment_service.service;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoCreateDTO;
import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.exception.RegraNegocioException;
import br.com.techmarket_payment_service.mapper.PagamentoMapper;
import br.com.techmarket_payment_service.model.Pagamento;
import br.com.techmarket_payment_service.model.enums.StatusPagamento;
import br.com.techmarket_payment_service.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final RabbitTemplate rabbitTemplate;

    public PagamentoService(PagamentoRepository pagamentoRepository, RabbitTemplate rabbitTemplate) {
        this.pagamentoRepository = pagamentoRepository;
        this.rabbitTemplate = rabbitTemplate;
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
    public void cadastrarPagamento(PagamentoCreateDTO pagamentoDTO) {
        Pagamento pagamento = PagamentoMapper.converterCreateDTOParaEntity(pagamentoDTO);
        pagamentoRepository.save(pagamento);
    }

    @Transactional
    public PagamentoResponseDTO confirmarPagamento(Long idPedido) {
        Pagamento pagamentoEntity = pagamentoRepository.findByIdPedido(idPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento com o IdPedido: " + idPedido + " não encontrado"));

        if (pagamentoEntity.getStatusPagamento().name().equals("CANCELADO")) {
            throw new RegraNegocioException("Pagamento já cancelado não pode ter o status alterado");
        }

        pagamentoEntity.setStatusPagamento(StatusPagamento.APROVADO);
        pagamentoRepository.save(pagamentoEntity);

        PagamentoResponseDTO pagamentoResponseDTO = PagamentoMapper.converterParaResponseDTO(pagamentoEntity);
        System.out.println("Enviando pedido cadastrado: " + pagamentoResponseDTO);
        rabbitTemplate.convertAndSend("pagamento.exchange", "pagamento.confirmado", pagamentoResponseDTO);

        return pagamentoResponseDTO;
    }

    @Transactional
    public void cancelarPagamento(Long idPedido) {
        Pagamento pagamento = pagamentoRepository.findByIdPedido(idPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento com o IdPedido: " + idPedido + " não encontrado"));

        pagamento.setStatusPagamento(StatusPagamento.CANCELADO);
        pagamentoRepository.save(pagamento);
    }
}
