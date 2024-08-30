package vendas.application.rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vendas.application.domain.entity.Produto;
import vendas.application.domain.repository.Produtos;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private Produtos produtos;

    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @GetMapping("{id}")
    @Operation(summary = "Produto por ID")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Erro as buscar produto")
    })
    public Produto getProdutoById(
            @Parameter(description = "ID do produto")
            @PathVariable Integer id){
        return produtos
                .findById(id)
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Salvar Produto")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "201", description = "Produto salvo"),
            @ApiResponse(responseCode = "400", description = "Erro as salvar produto")
    })
    public Produto save(@RequestBody @Valid Produto produto) {
        return produtos.save(produto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir Produto")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "204", description = "Produto excluído"),
            @ApiResponse(responseCode = "400", description = "Erro as excluir produto")
    })
    public void delete(
            @Parameter(description = "ID do produto")
            @PathVariable Integer id ){
        produtos.findById(id)
                .map(produto -> {produtos.delete(produto);
                    return produto;
                })
                .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Editar Produto")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "204", description = "Produto editado"),
            @ApiResponse(responseCode = "400", description = "Erro as editar produto")
    })
    public void update(
            @Parameter(description = "ID do produto")
            @PathVariable Integer id,
            @RequestBody @Valid Produto produto) {
        produtos.findById(id)
                .map(produtoExistente -> {
                    produto.setId(produtoExistente.getId());
                    produtos.save(produto);
                    return produtoExistente;
                }) .orElseThrow(() ->  new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Produto não encontrado"));
    }

    @GetMapping
    @Operation(summary = "Buscar lista de produtos")
    @ApiResponses(value  = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrado"),
            @ApiResponse(responseCode = "404", description = "Erro as buscar produtos")
    })
    public List<Produto> find(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);
        return produtos.findAll(example);

    }
}
