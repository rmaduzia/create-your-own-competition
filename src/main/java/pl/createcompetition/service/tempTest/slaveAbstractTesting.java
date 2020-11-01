package pl.createcompetition.service.tempTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.createcompetition.searchQuery.SearchCriteria;
import pl.createcompetition.searchQuery.UserSearchQueryCriteriaConsumer;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class slaveAbstractTesting extends mainAbstractTesting{

    /*
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("temptest");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

     */
   // @PersistenceContext()
   // private EntityManager entityManager;


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("temptest");
    EntityManager entityManager = emf.createEntityManager();


    /*
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("example-unit");

    EntityManager entityManager = entityManagerFactory.createEntityManager();


     */
    //EntityManager entityManager = emf.createEntityManager();




    @Override
    public List<?> gty(String search, Class t) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<?> query = builder.createQuery(t);
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

    }



/*
    @Override
    public List<?> findQuery(String search, Class t){

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<t> query = builder.createQuery(T);
        final Root r = query.from(T);

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

        return entityManager.createQuery(query).getResultStream().collect(Collectors.toList()
}

 */



}
