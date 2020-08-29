package pl.createcompetition.searchQuery;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteriaTesting {
    private String key;
    private String operation;
    private Object value;

}