package pl.createcompetition.service.tempTest;

import org.springframework.stereotype.Service;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.searchQuery.SearchCriteria;
import pl.createcompetition.searchQuery.UserSearchQueryCriteriaConsumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetQueryUserService extends GetQueryAbstractService<UserDetail, UserDetail.UserDetailDto> {

    @Override
    protected Class<UserDetail> getClazz() {
        return UserDetail.class;
    }

    @Override
    protected Predicate getPredicate(Predicate predicate, CriteriaBuilder builder, Root r, List<SearchCriteria> params) {
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.forEach(searchConsumer);
        return searchConsumer.getPredicate();
    }

    @Override
    protected List<UserDetail.UserDetailDto> getDtos(CriteriaQuery<UserDetail> query) {
        return entityManager
                .createQuery(query)
                .getResultStream()
                .map(UserDetail::toUserDetailDto)
                .collect(Collectors.toList());
    }
}
