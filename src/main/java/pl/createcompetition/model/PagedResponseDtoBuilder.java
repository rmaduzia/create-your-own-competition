package pl.createcompetition.model;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
public class PagedResponseDtoBuilder {

    private List<?> listDto;
    private Page<?> entityPage;


    public static PagedResponseDtoBuilder create() {
        return new PagedResponseDtoBuilder();
    }

    public PagedResponseDtoBuilder listDto(List<?> listDto) {
        this.listDto = listDto;
        return this;
    }

    public PagedResponseDtoBuilder entityPage(Page<?> entityPage) {
        this.entityPage = entityPage;
        return this;
    }

    public PagedResponseDto<?> build() {
        return new PagedResponseDto<>(
                listDto,
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.isLast());
    }
}