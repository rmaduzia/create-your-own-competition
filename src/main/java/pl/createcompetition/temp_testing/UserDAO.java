package pl.createcompetition.temp_testing;

import org.springframework.stereotype.Repository;
import pl.createcompetition.model.UserDetail;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository

public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDetail> searchUser(final List<SearchCriteria> params) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<UserDetail> query = builder.createQuery(UserDetail.class);
        final Root r = query.from(UserDetail.class);

        Predicate predicate = builder.conjunction();
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.stream().forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void save(final UserDetail entity) {
        entityManager.persist(entity);
    }
}
