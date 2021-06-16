package com.example.book.dto.response;

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
    private String id;
}
