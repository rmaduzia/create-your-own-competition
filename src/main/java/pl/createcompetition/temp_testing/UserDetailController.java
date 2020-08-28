package pl.createcompetition.temp_testing;

import com.google.common.base.Joiner;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.User;
import pl.createcompetition.model.UserDetail;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping(value = "/temptest/")
public class UserDetailController {


    @Autowired
    private IUserDAO service;

    @Autowired
    private UserRepositoryTemp dao;

    @Autowired
    private UserDetailRepositoryTemp myUserRepository;

    public UserDetailController() {
        super();
    }

    // API - READ

    @RequestMapping(method = RequestMethod.GET, value = "/temp_user")
    @ResponseBody
    public List<UserDetail> search(@RequestParam(value = "search", required = false) String search) {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return service.searchUser(params);
    }


    protected Specification<UserDetail> resolveSpecificationFromInfixExpr(String searchParameters) {
        CriteriaParser parser = new CriteriaParser();
        GenericSpecificationsBuilder<UserDetail> specBuilder = new GenericSpecificationsBuilder<>();
        return specBuilder.build(parser.parse(searchParameters), UserSpecification::new);
    }

    protected Specification<UserDetail> resolveSpecification(String searchParameters) {

        UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\p{Punct}?)(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(searchParameters + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(5), matcher.group(4), matcher.group(6));
        }
        return builder.build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/temp_myusers")
    @ResponseBody
    public Iterable<UserDetail> findAllByQuerydsl(@RequestParam(value = "search") String search) {
        MyUserPredicatesBuilder builder = new MyUserPredicatesBuilder();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        return myUserRepository.findAll(exp);
    }

    // API - WRITE
/*
    @RequestMapping(method = RequestMethod.POST, value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody UserDetail resource) {
        Preconditions.checkNotNull(resource);
        dao.save(resource);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/myusers")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMyUser(@RequestBody UserDetail resource) {
        Preconditions.checkNotNull(resource);
        myUserRepository.save(resource);

    }

 */
    

}
