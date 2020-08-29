package pl.createcompetition.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.createcompetition.model.UserDetail;
import pl.createcompetition.repository.UserDetailsRepository;
import pl.createcompetition.dao.IUserDetailsDAO;
import pl.createcompetition.searchQuery.SearchCriteriaTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller
@RequestMapping(value = "/user_details")
public class UserDetailsController {


    @Autowired
    private IUserDetailsDAO service;

    @Autowired
    private UserDetailsRepository myUserRepository;

    public UserDetailsController() {
        super();
    }

    // API - READ
    //@RequestMapping(method = RequestMethod.GET, value = "/1")

    @GetMapping
    @ResponseBody
    public List<UserDetail> search(@RequestParam(value = "search", required = false) String search) {

        List<SearchCriteriaTesting> params = new ArrayList<SearchCriteriaTesting>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteriaTesting(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }
        return service.searchUser(params);

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
