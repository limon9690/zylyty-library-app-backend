package com.limon.library_app.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BorrowBookRequest {
    private String isbn;
}
