package br.com.techmarket_payment_service.controller;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.service.PagamentoService;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public ResponseEntity<Page<PagamentoResponseDTO>> listarTodosPagamentos(@PageableDefault(size = 10) Pageable paginacao) {
        Page<PagamentoResponseDTO> pagamentos = pagamentoService.obterTodosPagamentos(paginacao);
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> buscarPagamentoPorId(@PathVariable @NotNull Long id) {
        PagamentoResponseDTO pagamento = pagamentoService.buscarPagamentoPorId(id);
        return ResponseEntity.ok(pagamento);
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<PagamentoResponseDTO> buscarPagamentoPorIdPedido(@PathVariable @NotNull Long idPedido) {
        PagamentoResponseDTO pagamento = pagamentoService.buscarPagamentoPorIdPedido(idPedido);
        return ResponseEntity.ok(pagamento);
    }

    @PatchMapping("/{idPedido}/confirmar")
    public ResponseEntity<PagamentoResponseDTO> confirmarPagamento(@PathVariable @NotNull Long idPedido) {
        PagamentoResponseDTO pagamento = pagamentoService.confirmarPagamento(idPedido);
        return ResponseEntity.ok(pagamento);
    }
}
