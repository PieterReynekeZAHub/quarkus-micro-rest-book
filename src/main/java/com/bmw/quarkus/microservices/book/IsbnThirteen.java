package com.bmw.quarkus.microservices.book;

import jakarta.json.bind.annotation.JsonbProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IsbnThirteen {
    @JsonbProperty("isbn_13")
    public String isbn13;

}
