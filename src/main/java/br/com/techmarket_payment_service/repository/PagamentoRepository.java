package br.com.techmarket_payment_service.repository;

import br.com.techmarket_payment_service.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByIdPedido(Long idPedido);
}
