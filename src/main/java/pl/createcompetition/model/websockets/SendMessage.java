package pl.createcompetition.model.websockets;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.createcompetition.util.StringUtils;

@Getter
@Setter
@NoArgsConstructor
public class SendMessage {

    private String from;
    private String text;
    private String recipient;
    private String time;



    public SendMessage(String from, String text, String recipient) {
        this.from = from;
        this.text = text;
        this.recipient = recipient;
        this.time = StringUtils.getCurrentTimeStamp();
    }

}
