package br.com.techmarket_payment_service.controller;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoResponseDTO;
import br.com.techmarket_payment_service.service.PagamentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PagamentoResponseDTO> buscarPagamentoPorId(@PathVariable Long id) {
        PagamentoResponseDTO pagamento = pagamentoService.buscarPagamentoPorId(id);
        return ResponseEntity.ok(pagamento);
    }

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<PagamentoResponseDTO> buscarPagamentoPorIdPedido(@PathVariable Long idPedido) {
        PagamentoResponseDTO pagamento = pagamentoService.buscarPagamentoPorIdPedido(idPedido);
        return ResponseEntity.ok(pagamento);
    }
}
