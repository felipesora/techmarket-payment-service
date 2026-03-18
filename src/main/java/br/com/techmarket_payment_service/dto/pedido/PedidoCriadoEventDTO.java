package br.com.techmarket_payment_service.dto.pedido;

import br.com.techmarket_payment_service.model.enums.MetodoPagamento;

import java.math.BigDecimal;
import java.util.List;

public record PedidoCriadoEventDTO(
        Long pedidoId,
        BigDecimal valorTotal,
        MetodoPagamento metodoPagamento,
        List<ItemPedidoEventDTO> itens
) {
}
