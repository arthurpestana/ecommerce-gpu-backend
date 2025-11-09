package org.acme.dtos.gpu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


import org.acme.dtos.image.ImageRequestDTO;
import org.acme.dtos.technology.TechnologyRequestDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GpuRequestDTO(

    @NotBlank(message = "O nome da GPU é obrigatório")
    @Size(max = 255, message = "O nome pode ter no máximo 255 caracteres")
    String name,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
    String description,

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que 0")
    BigDecimal price,

    @NotNull(message = "O campo isActive é obrigatório")
    Boolean isActive,

    @NotNull(message = "A quantidade disponível é obrigatória")
    @Min(value = 0, message = "A quantidade deve ser positiva")
    Integer availableQuantity,

    @NotNull(message = "A memória é obrigatória")
    @Min(value = 1, message = "A memória deve ser positiva")
    Integer memory,

    @NotBlank(message = "A arquitetura é obrigatória")
    String architecture,

    @NotNull(message = "O consumo de energia é obrigatório")
    @Min(value = 1, message = "O consumo de energia deve ser positivo")
    Integer energyConsumption,

    @NotNull(message = "O modelo é obrigatório")
    @NotBlank(message = "O ID do modelo é obrigatório")
    String modelId,
    
    Set<TechnologyRequestDTO> technologies,

    Set<String> categoryIds
) {}
