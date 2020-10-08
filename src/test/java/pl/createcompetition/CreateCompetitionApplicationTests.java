package pl.createcompetition;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;


import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;


@SpringBootTest
class CreateCompetitionApplicationTests {



/*
     @Autowired
     private UserDetailRepositoryTemp userDetailRepositoryTemp;

    @Test
    public void trepooooo(){

        User user = new User("login2222", "haslo%123");
        userRepository.save(user);

        Optional<User> klop = userRepository.findByUsername("login2222");
        System.out.println(klop.get());

        UserDetail userDetail = new UserDetail();
        userDetail.setUser(klop.get());
        userDetail.setAge(20);
        userDetail.setCity("Gdynia");
        userDetailRepository.save(userDetail);

        System.out.println(userDetail.toString());


        final UserSpecification spec = new UserSpecification(new SpecSearchCriteria("city", SearchOperation.EQUALITY, "Gdynia"));
        final List<UserDetail> results = userDetailRepositoryTemp.findAll(Specification
                .where(spec));
        assertThat(userDetail, isIn(results));


 */




        /*
        Specification<UserDetail> where = Specification.where(DocumentSpecs.idEq(1L));
        Page<UserDetailRepositoryTempNew.DocumentWithoutParent> page = userDetailRepositoryTempNew.findAll(where, UserDetailRepositoryTempNew.DocumentWithoutParent.class, PageRequest.of(0, 10));
        System.out.println(page.getContent().get(0).getCity());
        Assertions.assertThat(page.getContent().get(0).getCity()).isEqualTo("Gdynia");
         */

     /*
    @Test
    public void testttttttt(){

        User user = new User("login2222", "haslo%123");
        userRepository.save(user);

        Optional<User> klop = userRepository.findByUsername("login2222");
        System.out.println(klop.get());

        UserDetail userDetail = new UserDetail();
        userDetail.setUser(klop.get());
        userDetail.setAge(20);
        userDetail.setCity("Gdynia");
        userDetailRepository.save(userDetail);

        System.out.println(userDetail.toString());


        Specification<UserDetail> where = Specification.where(DocumentSpecs.idEq(1L));
        Page<UserDetailRepositoryTempNew.DocumentWithoutParent> page = userDetailRepositoryTempNew.findAll(where, UserDetailRepositoryTempNew.DocumentWithoutParent.class, PageRequest.of(0, 10));
        System.out.println(page.getContent().get(0).getCity());
        Assertions.assertThat(page.getContent().get(0).getCity()).isEqualTo("Gdynia");
      */

    /*
    @Test
    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() {
        final List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("firstName", ":", "john"));
        params.add(new SearchCriteria("lastName", ":", "doe"));

        final List<User> results = userApi.searchUser(params);

        assertThat(userJohn, isIn(results));
        assertThat(userTom, not(isIn(results)));
    }


     */
/*
    @Test
    public void findAllWithOpenProjection() {
        System.out.println("test");
        Specification<UserDetail> where = Specification.where(DocumentSpecs.idEq(2L));
        Page<UserDetailRepositoryTempNew.DocumentWithoutParent> page = documentRepository.findAll(where, UserDetailRepositoryTempNew.DocumentWithoutParent.class, PageRequest.of(0, 10));
        Assertions.assertThat(page.getContent().get(0).getCity()).isEqualTo("Gdynia");
 */
    /*
    @Test
    public void specificationWithProjection() {
        Specification<UserDetail> where = Specification.where(DocumentSpecs.idEq(1L));
        Page<UserDetailRepositoryTemp.DocumentWithoutParent> all = UserDetailRepositoryTemp.findAll(where, UserDetailRepositoryTemp.DocumentWithoutParent.class, new PageRequest(0,10));
        Assertions.assertThat(all).isNotEmpty();
    }

     */



}