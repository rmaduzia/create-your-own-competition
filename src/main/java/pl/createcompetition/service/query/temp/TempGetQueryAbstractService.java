package pl.createcompetition.service.query.temp;

import pl.createcompetition.model.UserDetail;
import pl.createcompetition.service.query.SearchCriteria;
import pl.createcompetition.service.query.UserSearchQueryCriteriaConsumer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class TempGetQueryAbstractService<B extends Intt<R>, R> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Predicate getPredicate(Predicate predicate, CriteriaBuilder builder, Root r, List<SearchCriteria> params);

    //protected abstract List<R> getList(CriteriaQuery<B> query);

    public List<R> execute(Class<B> encja, String search, Function<B, R> mapper) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<B> query = builder.createQuery(encja);
        final Root r = query.from(encja);


        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }

        Predicate predicate = builder.conjunction();
        predicate = getPredicate(predicate, builder, r, params);
        query.where(predicate);

        var mapperFirst = new Mapper<B, R>();

        return mapperFirst.map(entityManager
                .createQuery(query)
                .getResultStream()
                .collect(Collectors.toList()));

    }
    static class Mapper<A extends Intt<B>, B> {
        public List<B> map(List<A> from) {
            return from.stream()
                    .map(Intt::map)
                    .collect(toList());
        }
    }
}