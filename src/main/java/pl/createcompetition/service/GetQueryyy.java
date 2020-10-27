package pl.createcompetition.service;

import pl.createcompetition.model.UserDetail;
import pl.createcompetition.searchQuery.SearchCriteria;
import pl.createcompetition.searchQuery.UserSearchQueryCriteriaConsumer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GetQueryyy {

    @PersistenceContext
    private EntityManager entityManager;

    public  <T> List<?> getQuery (String search, Class<T> t) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(t);
        final Root r = query.from(t);

        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }

        Predicate predicate = builder.conjunction();
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, r);
        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        return entityManager.createQuery(query).getResultStream().collect(Collectors.toList());


        //return entityManager.createQuery(query).getResultStream().map(UserDetail::toUserDetailDto).collect(Collectors.toList());

    }


}
