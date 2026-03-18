package br.com.techmarket_payment_service.mapper;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoCreateDTO;
import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.model.Pagamento;
import br.com.techmarket_payment_service.model.enums.StatusPagamento;

import java.time.LocalDateTime;

public final class PagamentoMapper {

    private PagamentoMapper() {}

    public static Pagamento converterCreateDTOParaEntity(PagamentoCreateDTO dto) {

        Pagamento pagamento = new Pagamento();
        pagamento.setIdPedido(dto.idPedido());
        pagamento.setValorTotal(dto.valorTotal());
        pagamento.setMetodoPagamento(dto.metodoPagamento());
        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        pagamento.setDataCriacao(LocalDateTime.now());

        return pagamento;
    }

    public static PagamentoResponseDTO converterParaResponseDTO(Pagamento pagamento) {

        return new PagamentoResponseDTO(
                pagamento.getId(),
                pagamento.getIdPedido(),
                pagamento.getValorTotal(),
                pagamento.getMetodoPagamento(),
                pagamento.getStatusPagamento(),
                pagamento.getDataCriacao()
        );
    }
}
