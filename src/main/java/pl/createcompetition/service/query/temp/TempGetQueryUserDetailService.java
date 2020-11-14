package pl.createcompetition.service.query.temp;

import org.springframework.stereotype.Service;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.service.query.GetQueryAbstractService;
import pl.createcompetition.service.query.SearchCriteria;
import pl.createcompetition.service.query.UserSearchQueryCriteriaConsumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

@Service
//public class TempGetQueryUserDetailService<B,R extends TempGetQueryAbstractService<B, R>> {
//public class TempGetQueryUserDetailService extends TempGetQueryAbstractService<UserDetail, UserDetail.UserDetailDto> {
public class TempGetQueryUserDetailService extends TempGetQueryAbstractService<UserDetail, UserDetail.UserDetailDto> {


    @Override
    public  Predicate getPredicate(Predicate predicate, CriteriaBuilder builder, Root r, List<SearchCriteria> params) {
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.forEach(searchConsumer);
        return searchConsumer.getPredicate();
    }


}