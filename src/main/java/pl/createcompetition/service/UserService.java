package pl.createcompetition.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.createcompetition.util.email.Mail;
import pl.createcompetition.util.email.MailService;
import pl.createcompetition.util.email.TemplateValues;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.User;
import pl.createcompetition.payload.ApiResponse;
import pl.createcompetition.payload.ChangeMailRequest;
import pl.createcompetition.payload.ChangePasswordRequest;
import pl.createcompetition.payload.interfaces.InterfaceChangeRequest;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.CurrentUser;
import pl.createcompetition.security.UserPrincipal;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {
    final private UserRepository userDao;
    final private MailService mailService;

    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userDao.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    public ResponseEntity<ApiResponse> changeEmail(ChangeMailRequest request) {
        User user = getUserForChange(request);
        Mail mail = sendInformationEmile(user,"email","https://www.google.pl/", request.getEmail());
        user.setEmail(request.getEmail());
        user = userDao.save(user);
        return changeResponse(request.getEmail().equals(user.getEmail()),"Email", mail);
    }

    public ResponseEntity<ApiResponse> changePassword(ChangePasswordRequest request) {
        User user = getUserForChange(request);
        Mail mail = sendInformationEmile(user,"password","https://www.google.pl/",request.getNewPassword());
        user.setPassword(request.getNewPassword());
        user = userDao.save(user);
        return changeResponse(request.getNewPassword().equals(user.getPassword())," Password", mail);
    }

    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public Iterable<User> getUsersByProps(
            Optional<Long> publicId,
            Optional<String> name,
            Optional<String> age,
            Optional<String> email) {

        // This is just creating userSpecification to be able to use and, doing nothing to query
        Specification<User> userSpecification = Specification.where((Specification<User>) (root, criteriaQuery, criteriaBuilder) -> root.isNotNull());

    /*    // Creating specification based on props. If prop is null than we're not including it in query
        publicId.ifPresent(var -> userSpecification.and(publicIdEquals(var)));
        name.ifPresent(var -> userSpecification.and(nameEquals(var)));
        age.ifPresent(var -> userSpecification.and(getSpecificationForAge(UrlNumberParser.parse(var))));
        email.ifPresent(var -> userSpecification.and(emailEquals(var)));
     */
        //return userDao.findAll(userSpecification);
        return userDao.findAll();
    }


    /*
    private Specification<User> getSpecificationForAge(UrlNumberParser.UrlNumberParserResponse<Integer> numberParserResponse) {
        switch (numberParserResponse.getNumberSpecifier()) {
            case EQUAL:
                return ageEquals(numberParserResponse.getNumber());
            case NOT_EQUAL:
                return ageNotEqual(numberParserResponse.getNumber());
            case GRATER:
                return ageGraterThan(numberParserResponse.getNumber());
            case GRATER_EQUAL:
                return ageGraterEqualThan(numberParserResponse.getNumber());
            case LOWER:
                return ageLowerThan(numberParserResponse.getNumber());
            case LOWER_EQUAL:
                return ageLowerEqualThan(numberParserResponse.getNumber());
            default:
                throw new AgeSpecifierNotFoundException();
        }
    }

     */

    public User save(User user) {
        return userDao.save(user);
    }

    private User getUserForChange(InterfaceChangeRequest changeRequest){
        return userDao.findByIdAndPassword(changeRequest.getUserId(), changeRequest.getPassword())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", changeRequest.getUserId()));
    }

    private ResponseEntity<ApiResponse> changeResponse(boolean changeCondition, String parameter, Mail mail) {
        if (changeCondition) {
            if (mail != null){
                mailService.send(mail);
            }
            return ResponseEntity.ok(new ApiResponse(true,parameter + " has change"));
        }
        throw new RuntimeException(parameter + " hasn't change");
    }


    private Mail sendInformationEmile(User user, String data, String link, String value) {
        TemplateValues values = TemplateValues.builder()
                .changedData(data)
                .changeDataLink(link)
                .dataValue(user.getUserName())
                .name(value).build();

        return new Mail(user.getEmail(),values);
    }
}
