package pl.createcompetition.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.searchQuery.SearchCriteria;
import pl.createcompetition.searchQuery.UserSearchQueryCriteriaConsumer;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.service.tempTest.slaveAbstractTesting;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.lang.reflect.*;

@Service
public class UserDetailService {

    private UserDetailRepository userDetailRepository;

    private UserRepository userRepository;

    UserDetailService(UserDetailRepository userDetailRepository, UserRepository userRepository ) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
    }

    public ResponseEntity<?> addUserDetail(UserDetail userDetail, UserPrincipal userPrincipal)  {

        Optional<User> foundUser = findUser(userPrincipal.getId());

        if(foundUser.isPresent()){
            userDetail.setUser(foundUser.get());
            userDetail.setId(foundUser.get().getId());
        }
        return ResponseEntity.ok(userDetailRepository.save(userDetail));
    }

    public ResponseEntity<?> updateUserDetail(UserDetail userDetail, UserPrincipal userPrincipal){

        findUser(userPrincipal.getId());
        userDetail.setId(userPrincipal.getId());

        return ResponseEntity.ok(userDetailRepository.save(userDetail));
    }

    public ResponseEntity<?> deleteUserDetail(UserDetail userDetail, UserPrincipal userPrincipal) {

        findUser(userPrincipal.getId());

        if (userDetail.getId().equals(userPrincipal.getId())) {
                userDetailRepository.deleteById(userPrincipal.getId());
            return ResponseEntity.noContent().build();
            }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    public Optional<User> findUser(Long id) {
        return Optional.ofNullable(userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("UserProfile", "ID", id)));
    }


   // @PersistenceContext
//    private EntityManager entityManager;


    public List<?> searchUser(String search) {

        //return entityManager.createQuery(query).getResultStream().map(UserDetail::toUserDetailDto).collect(Collectors.toList());


        //List<?> result = getQuery(search, UserDetail.class);

        //return getQuery(search, UserDetail.class).stream().map(UserDetail::toUserDetailDto);
       // result.stream().map(UserDetail::toUserDetailDto);
        //GetQueryyy tmp = new GetQueryyy().getQuery(search, UserDetail.class);

        //return new GetQueryyy().getQuery(search, UserDetail.class);


        //GetQueryyy tmp = new GetQueryyy();
        //return tmp.getQuery(search, UserDetail.class);]



        //return  getQuery(search, UserDetail.class);

        slaveAbstractTesting ftp = new slaveAbstractTesting();

        return ftp.gty(search, UserDetail.class);


    }
/*
    public <T> List<?> getQuery (String search, Class<T> t) {


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

        //return entityManager.createQuery(query).getResultStream().collect(Collectors.toList());

        //TODO HAVE TO FIND OUT IF I SHOULD MAP HERE OR INSIDE THIS FUNCTION OR OUTSIDE

        //String toDto = "to"+t.getSimpleName()+ "Dto";
        //MethodType methodType = MethodType.methodType(t);
      //  MethodHandles.Lookup lookup = MethodHandles.lookup();
     //   MethodHandle handle = lookup.findVirtual(t, toDto, methodType);

       // Method method = t.getMethod(toDto, null);

      //  t.getMethod(toDto,null);

      //  Method methodInfo = t.getMethod(toDto);


      //  Consumer<T> consumer = T::methodInfo

        //return entityManager.createQuery(query).getResultStream().collect(Collectors.toList());

    }
 */




}
