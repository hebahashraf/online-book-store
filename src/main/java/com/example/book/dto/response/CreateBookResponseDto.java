package com.example.book.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookResponseDto {

    @NotNull
    @Schema(description = "unique id of the book. The id can be used to retrieve more details about the book",
            required = true, example = "1")
    private String id;
}
