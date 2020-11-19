package pl.createcompetition.service.query;

import pl.createcompetition.model.PageModel;
import pl.createcompetition.model.PagedResponseDto;
import pl.createcompetition.model.PagedResponseDtoBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

//TODO HAVE TO REFACTOR PAGINATION CODE
public abstract class GetQueryAbstractService<B extends QueryDtoInterface<R>, R> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Predicate getPredicate(Predicate predicate, CriteriaBuilder builder, Root r, List<SearchCriteria> params);


    public PagedResponseDto<?> execute(Class<B> encja, String search, int pageNumber, int pageSize) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<B> query = builder.createQuery(encja);
        final Root r = query.from(encja);

        //page number zaczyna sie od 1


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


        //TODO HAVE TO REFACTOR PAGINATION CODE
/*
        CriteriaQuery<Long> countQuery = builder
                .createQuery(Long.class);
        countQuery.select(builder
                .count(countQuery.from(encja)));
        Long total_elements = entityManager.createQuery(countQuery)
                .getSingleResult();
 */

        TypedQuery typedQuery = entityManager.createQuery(query);
       // while (pageNumber < total_elements.intValue()) {
            typedQuery.setFirstResult(pageNumber);
            typedQuery.setMaxResults(pageSize);
            //pageNumber += pageSize;
      //  }

        List<B> typeQueryList = typedQuery.getResultList();

        int total_elements_final = typeQueryList.size();

        List<R> rsm = mapperFirst.map(typeQueryList);


        PageModel tms = new PageModel(pageNumber,pageSize,total_elements_final,1,true);


        return PagedResponseDtoBuilder.create()
                .listDto(rsm)
                .entityPage(tms)
                .build();



       /*

        List<B> returnList = entityManager
                .createQuery(query)
                .getResultStream()
                .collect(Collectors.toList());


        return mapperFirst.map(entityManager
                .createQuery(query)
                .getResultStream()
                .collect(Collectors.toList()));

        */
    }

    /*
    public List<R> execute(Class<B> encja, String search) {
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


     */


    static class Mapper<A extends QueryDtoInterface<B>, B> {
        public List<B> map(List<A> from) {
            return from.stream()
                    .map(QueryDtoInterface::map)
                    .collect(toList());
        }
    }

}