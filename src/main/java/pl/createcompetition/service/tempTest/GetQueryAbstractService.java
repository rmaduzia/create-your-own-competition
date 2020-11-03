package pl.createcompetition.service.tempTest;

import pl.createcompetition.searchQuery.SearchCriteria;

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

public abstract class GetQueryAbstractService<B, R> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<B> getClazz();

    protected abstract Predicate getPredicate(Predicate predicate, CriteriaBuilder builder, Root r, List<SearchCriteria> params);

    protected abstract List<R> getDtos(CriteriaQuery<B> query);

    public List<R> execute(String search) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<B> query = builder.createQuery(getClazz());
        final Root r = query.from(getClazz());

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

        return getDtos(query);
    }
}
