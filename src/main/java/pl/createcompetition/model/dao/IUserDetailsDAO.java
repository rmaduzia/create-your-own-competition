package pl.createcompetition.model.dao;

import pl.createcompetition.model.UserDetail;

import java.util.List;


public interface IUserDetailsDAO  {
    //List<UserDetail> searchUser(List<SearchCriteria> params);
    List<UserDetail> searchUser(String search);
    //void save(UserDetail entity);
}
