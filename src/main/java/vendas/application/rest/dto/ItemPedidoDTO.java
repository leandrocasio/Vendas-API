package vendas.application.rest.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemPedidoDTO {
    private Integer produto;
    private Integer quantidade;
}
