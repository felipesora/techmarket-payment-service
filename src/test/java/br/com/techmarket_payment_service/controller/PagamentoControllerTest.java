package br.com.techmarket_payment_service.controller;

import br.com.techmarket_payment_service.config.SecurityFilter;
import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.model.enums.MetodoPagamento;
import br.com.techmarket_payment_service.model.enums.StatusPagamento;
import br.com.techmarket_payment_service.service.PagamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagamentoService pagamentoService;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private PagamentoResponseDTO criarPagamentoMock() {
        return new PagamentoResponseDTO(
                1L,
                10L,
                BigDecimal.valueOf(200),
                MetodoPagamento.CARTAO_CREDITO,
                StatusPagamento.PENDENTE,
                null
        );
    }

    @Test
    void deveListarTodosPagamentos() throws Exception {
        PagamentoResponseDTO pagamento = criarPagamentoMock();

        Page<PagamentoResponseDTO> page = new PageImpl<>(List.of(pagamento));

        when(pagamentoService.obterTodosPagamentos(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id_pagamento").value(1L))
                .andExpect(jsonPath("$.content[0].id_pedido").value(10L));
    }

    @Test
    void deveBuscarPagamentoPorId() throws Exception {
        PagamentoResponseDTO pagamento = criarPagamentoMock();

        when(pagamentoService.buscarPagamentoPorId(1L))
                .thenReturn(pagamento);

        mockMvc.perform(get("/pagamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_pagamento").value(1L))
                .andExpect(jsonPath("$.id_pedido").value(10L));
    }

    @Test
    void deveBuscarPagamentoPorIdPedido() throws Exception {
        PagamentoResponseDTO pagamento = criarPagamentoMock();

        when(pagamentoService.buscarPagamentoPorIdPedido(10L))
                .thenReturn(pagamento);

        mockMvc.perform(get("/pagamentos/pedido/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_pedido").value(10L));
    }

    @Test
    void deveConfirmarPagamento() throws Exception {
        PagamentoResponseDTO pagamento = criarPagamentoMock();

        when(pagamentoService.confirmarPagamento(1L))
                .thenReturn(pagamento);

        mockMvc.perform(patch("/pagamentos/1/confirmar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_pagamento").value(1L))
                .andExpect(jsonPath("$.status_pagamento").value("PENDENTE")); // depende do mapper
    }

    @Test
    void deveRetornar404QuandoPagamentoNaoEncontrado() throws Exception {
        when(pagamentoService.buscarPagamentoPorId(1L))
                .thenThrow(new jakarta.persistence.EntityNotFoundException());

        mockMvc.perform(get("/pagamentos/1"))
                .andExpect(status().isNotFound());
    }
}