package br.com.techmarket_payment_service.amqp;

import br.com.techmarket_payment_service.dto.pagamento.PagamentoCreateDTO;
import br.com.techmarket_payment_service.dto.pedido.PedidoCanceladoEventDTO;
import br.com.techmarket_payment_service.dto.pedido.PedidoCriadoEventDTO;
import br.com.techmarket_payment_service.mapper.PagamentoMapper;
import br.com.techmarket_payment_service.service.PagamentoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {

    private final PagamentoService pagamentoService;

    public PedidoListener(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @RabbitListener(queues = "pedido.criado.pagamento.fila")
    public void recebePedidoCriado(PedidoCriadoEventDTO evento) {
        System.out.println("Mensagem recebida da fila de pedidos criados");
        System.out.println("Conteúdo: " + evento);

        PagamentoCreateDTO pagamentoCreateDTO = PagamentoMapper.converterEventoParaCreateDTO(evento);
        pagamentoService.cadastrarPagamento(pagamentoCreateDTO);
        System.out.println("Pagamento cadastrado!");
    }

    @RabbitListener(queues = "pedido.cancelado.pagamento.fila")
    public void recebePedidoCancelado(PedidoCanceladoEventDTO evento) {
        System.out.println("Mensagem recebida da fila de pedidos cancelados");
        System.out.println("Conteúdo: " + evento);


    }
}
