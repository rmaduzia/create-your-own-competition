package pl.createcompetition.model.projections;

import org.springframework.beans.factory.annotation.Value;
import pl.createcompetition.model.Gender;

public interface UserDetailProjection {

//    public String getCompetitionName();

    public String getUsername();

    public String getCity();

    public Integer getAge();

    public Gender getGender();




   // @Value("#User")
   // public String getOtherEntityName();
}


