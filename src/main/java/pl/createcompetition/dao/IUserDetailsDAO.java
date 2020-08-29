package pl.createcompetition.dao;

import pl.createcompetition.model.UserDetail;
import pl.createcompetition.searchQuery.SearchCriteriaTesting;

import java.util.List;


public interface IUserDetailsDAO  {
    List<UserDetail> searchUser(List<SearchCriteriaTesting> params);
    void save(UserDetail entity);

}
