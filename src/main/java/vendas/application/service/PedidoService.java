package vendas.application.service;

import vendas.application.domain.entity.Pedido;
import vendas.application.domain.enums.StatusPedido;
import vendas.application.rest.dto.PedidoDTO;

import java.util.Optional;

public interface PedidoService {
    Pedido salvar(PedidoDTO dto);
    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizaStatus(Integer id, StatusPedido statusPedido);
}
