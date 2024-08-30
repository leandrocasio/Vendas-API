package vendas.application.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vendas.application.domain.entity.ItemPedido;
import vendas.application.domain.entity.Pedido;
import vendas.application.domain.enums.StatusPedido;
import vendas.application.rest.dto.AtualizacaoStatusPedidoDTO;
import vendas.application.rest.dto.InformacaoItemPedidoDTO;
import vendas.application.rest.dto.InformacoesPedidoDTO;
import vendas.application.rest.dto.PedidoDTO;
import vendas.application.service.PedidoService;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {


    private PedidoService service;


    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar pedido")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "201", description = "Pedido salvo"),
            @ApiResponse(responseCode = "400", description = "Erro as salvar pedido")
    })
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    @Operation(summary = "Pedido por ID")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado para o ID informado")
    })
    public InformacoesPedidoDTO getById(
            @Parameter(description = "ID do pedido")
            @PathVariable Integer id) {
        return service
                .obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Atualizar pedido")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "201", description = "Pedido atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não atualizado")
    })
    public void updateStatus(
            @Parameter(description = "ID do pedido")
            @PathVariable Integer id,
            @RequestBody @Valid AtualizacaoStatusPedidoDTO dto) {
        String novoStatus = dto.getNovoStatus();
        service.atualizaStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        return InformacoesPedidoDTO
                .builder()
                .codigo(pedido.getId())
                .dataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(pedido.getCliente().getCpf())
                .nomeCliente(pedido.getCliente().getNome())
                .total(pedido.getTotal())
                .status(pedido.getStatus().name())
                .itens(converter(pedido.getItens()))
                .build();
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.EMPTY_LIST;
        }

        return itens.stream().map(
                item -> InformacaoItemPedidoDTO
                        .builder()
                        .descricaoProduto(item.getProduto().getDescricao())
                        .precoUnitario(item.getProduto().getPreco())
                        .quantidade(item.getQuantidade())
                        .build()
        ).collect(Collectors.toList());
    }
}
