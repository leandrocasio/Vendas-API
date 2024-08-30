package vendas.application.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vendas.application.domain.entity.Produto;

public interface Produtos extends JpaRepository<Produto, Integer> {
}
