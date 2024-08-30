package vendas.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vendas.application.domain.entity.ItemPedido;

public interface ItensPedido extends JpaRepository<ItemPedido, Integer> {
}
