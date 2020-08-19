package pl.createcompetition.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ResponseToken {

    private String tokenValue;

    public ResponseToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
