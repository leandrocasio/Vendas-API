package vendas.application.exception;

public class PedidoNaoEncontradoException extends RuntimeException  {

    public  PedidoNaoEncontradoException() {
        super("Pedido Não Encontrado");
    }
}
