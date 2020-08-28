package pl.createcompetition.temp_testing;

import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;

import java.util.List;


public interface IUserDAO  {
    List<UserDetail> searchUser(List<SearchCriteria> params);
    void save(UserDetail entity);


}
