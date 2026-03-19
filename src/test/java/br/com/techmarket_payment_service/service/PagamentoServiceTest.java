package br.com.techmarket_payment_service.service;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoCreateDTO;
import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.exception.RegraNegocioException;
import br.com.techmarket_payment_service.model.Pagamento;
import br.com.techmarket_payment_service.model.enums.MetodoPagamento;
import br.com.techmarket_payment_service.model.enums.StatusPagamento;
import br.com.techmarket_payment_service.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamento;

    @BeforeEach
    void setup() {
        pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setIdPedido(10L);
        pagamento.setValorTotal(BigDecimal.valueOf(200));
        pagamento.setMetodoPagamento(MetodoPagamento.CARTAO_CREDITO);
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
    }

    @Test
    void deveBuscarPagamentoPorIdComSucesso() {
        when(pagamentoRepository.findById(1L))
                .thenReturn(Optional.of(pagamento));

        PagamentoResponseDTO response = pagamentoService.buscarPagamentoPorId(1L);

        assertNotNull(response);
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoNaoEncontradoPorId() {
        when(pagamentoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                pagamentoService.buscarPagamentoPorId(1L)
        );
    }

    @Test
    void deveBuscarPagamentoPorIdPedidoComSucesso() {
        when(pagamentoRepository.findByIdPedido(10L))
                .thenReturn(Optional.of(pagamento));

        PagamentoResponseDTO response = pagamentoService.buscarPagamentoPorIdPedido(10L);

        assertNotNull(response);
    }

    @Test
    void deveLancarExcecaoQuandoPagamentoNaoEncontradoPorIdPedido() {
        when(pagamentoRepository.findByIdPedido(10L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                pagamentoService.buscarPagamentoPorIdPedido(10L)
        );
    }

    @Test
    void deveCadastrarPagamentoComSucesso() {
        PagamentoCreateDTO dto = new PagamentoCreateDTO(
                10L,
                BigDecimal.valueOf(200),
                MetodoPagamento.CARTAO_CREDITO
        );

        when(pagamentoRepository.save(any(Pagamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        pagamentoService.cadastrarPagamento(dto);

        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    void deveConfirmarPagamentoComSucesso() {
        when(pagamentoRepository.findById(1L))
                .thenReturn(Optional.of(pagamento));

        when(pagamentoRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResponseDTO response = pagamentoService.confirmarPagamento(1L);

        assertEquals(StatusPagamento.APROVADO, pagamento.getStatusPagamento());

        verify(pagamentoRepository).save(pagamento);
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq("pagamento.exchange"),
                eq("pagamento.confirmado"),
                any(Object.class)
        );

        assertNotNull(response);
    }

    @Test
    void naoDeveConfirmarPagamentoCancelado() {
        pagamento.setStatusPagamento(StatusPagamento.CANCELADO);

        when(pagamentoRepository.findById(1L))
                .thenReturn(Optional.of(pagamento));

        assertThrows(RegraNegocioException.class, () ->
                pagamentoService.confirmarPagamento(1L)
        );
    }

    @Test
    void deveCancelarPagamentoComSucesso() {
        when(pagamentoRepository.findByIdPedido(10L))
                .thenReturn(Optional.of(pagamento));

        when(pagamentoRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        pagamentoService.cancelarPagamento(10L);

        assertEquals(StatusPagamento.CANCELADO, pagamento.getStatusPagamento());
        verify(pagamentoRepository).save(pagamento);
    }

    @Test
    void deveLancarExcecaoAoCancelarPagamentoNaoEncontrado() {
        when(pagamentoRepository.findByIdPedido(10L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                pagamentoService.cancelarPagamento(10L)
        );
    }

    @Test
    void deveObterTodosPagamentos() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Pagamento> page = new PageImpl<>(List.of(pagamento));

        when(pagamentoRepository.findAll(pageable)).thenReturn(page);

        Page<PagamentoResponseDTO> response = pagamentoService.obterTodosPagamentos(pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }
}