package br.com.techmarket_payment_service.dto.pedido;

public record ItemPedidoEventDTO(
        String produtoIdMongo,
        Integer quantidade
) {
}
