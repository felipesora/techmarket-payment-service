package br.com.techmarket_payment_service.dto.pagamento;

import br.com.techmarket_payment_service.model.enums.MetodoPagamento;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PagamentoCreateDTO (
        @NotNull(message = "O id pedido é obrigatório")
        @JsonProperty("id_pedido")
        Long idPedido,

        @NotNull(message = "O valor do pagamento obrigatório")
        @JsonProperty("valor_total")
        BigDecimal valorTotal,

        @NotNull(message = "O método de pagamento obrigatório")
        @JsonProperty("metodo_pagamento")
        MetodoPagamento metodoPagamento
) {
}
