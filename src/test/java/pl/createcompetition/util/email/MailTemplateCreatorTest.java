package pl.createcompetition.util.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MailTemplateCreatorTest {

    @Autowired
    MailTemplateCreator creator;

    @Test
    void should_BuildEmailTemplate() {

        TemplateValues values = TemplateValues.builder()
                .changeDataLink("test link")
                .changedData("test Data")
                .dataValue("test value")
                .name("test name").build();

        String emailHtml = creator.buildEmailTemplate(values);

        assertThat(emailHtml).isNotEmpty();
    }
}

