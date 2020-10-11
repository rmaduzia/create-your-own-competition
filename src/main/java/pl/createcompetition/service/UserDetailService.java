package pl.createcompetition.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.dao.IUserDetailsDAO;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.searchQuery.SearchCriteria;
import pl.createcompetition.searchQuery.UserSearchQueryCriteriaConsumer;
import pl.createcompetition.security.UserPrincipal;
import pl.createcompetition.supportingMethods.getLogedUserName;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserDetailService implements IUserDetailsDAO {

    private UserDetailRepository userDetailRepository;

    private UserRepository userRepository;

    UserDetailService(UserDetailRepository userDetailRepository, UserRepository userRepository ) {
        this.userRepository = userRepository;
        this.userDetailRepository = userDetailRepository;
    }

    public ResponseEntity<?> addUserDetail(UserDetail userDetail, UserPrincipal userPrincipal)  {

        //String userName = new getLogedUserName().username;
        Optional<User> foundUser = findUser(userPrincipal.getId());

        if(foundUser.isPresent()){
            userDetail.setUser(foundUser.get());
            userDetail.setId(foundUser.get().getId());
        }
        //userDetailRepository.save(userDetail);
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


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserDetail> searchUser(String search) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<UserDetail> query = builder.createQuery(UserDetail.class);
        final Root r = query.from(UserDetail.class);

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

        return entityManager.createQuery(query).getResultList();
    }


}
