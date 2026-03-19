package br.com.techmarket_payment_service.model;

import br.com.techmarket_payment_service.model.enums.MetodoPagamento;
import br.com.techmarket_payment_service.model.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TM_PAGAMENTOS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tm_pagamento_seq")
    @SequenceGenerator(name = "tm_pagamento_seq", sequenceName = "tm_pagamento_seq", allocationSize = 1)
    @Column(name = "id_pagamento")
    private Long id;

    @Column(name = "id_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status_pagamento")
    private StatusPagamento statusPagamento;

    @Column(nullable = false, name = "data_criacao")
    private LocalDateTime dataCriacao;
}
