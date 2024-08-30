package vendas.application.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vendas.application.domain.entity.Cliente;
import vendas.application.domain.repository.Clientes;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    private Clientes clientes;

    public ClienteController(Clientes clientes) {
        this.clientes = clientes;
    }

    @GetMapping("{id}")
    @Operation(summary = "Obter detalhes de um cliente")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado")
    })
    public Cliente getClienteById(
            @Parameter(description = "ID do cliente")
            @PathVariable Integer id){
      return clientes
              .findById(id)
              .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Cliente não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salva um novo cliente")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "201", description = "Cliente salvo com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente) {
        return clientes.save(cliente);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui o cliente")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "204", description = "Cliente exluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao excluir")
    })
    public void delete(
            @Parameter(description = "ID do cliente")
            @PathVariable Integer id ){
        clientes.findById(id)
                .map(cliente -> {clientes.delete(cliente);
                return cliente;
                })
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @PutMapping("{id}")
    @Operation(summary = "Editar cliente")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "204", description = "Editar cliente com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro ao editar")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
                @Parameter(description = "ID do cliente")
                @PathVariable Integer id,
                @RequestBody @Valid Cliente cliente) {
             clientes.findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                    return clienteExistente;
                }) .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @GetMapping
    @Operation(summary = "Buscar lista de clientes")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrado"),
            @ApiResponse(responseCode = "404", description = "Erro as buscar clientes")
    })
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);
        return clientes.findAll(example);

    }
}
