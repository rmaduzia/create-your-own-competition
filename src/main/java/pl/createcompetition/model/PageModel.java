package pl.createcompetition.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PageModel {
    private int Number;
    private int Size;
    private int TotalElements;
    private int TotalPages;
    private boolean Last;
}
