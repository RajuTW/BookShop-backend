package com.tw.bootcamp.bookshop.book;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class CreateBookRequest {
    List<BookRequest> books  = new ArrayList<BookRequest>();
}
