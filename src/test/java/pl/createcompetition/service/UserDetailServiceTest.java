package pl.createcompetition.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import pl.createcompetition.exception.ResourceNotFoundException;
import pl.createcompetition.model.*;
import pl.createcompetition.payload.PaginationInfoRequest;
import pl.createcompetition.repository.UserDetailRepository;
import pl.createcompetition.repository.UserRepository;
import pl.createcompetition.security.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserDetailServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserDetailRepository userDetailRepository;
    @InjectMocks
    UserDetailService userDetailService;

    User user;
    UserDetail userDetail;
    UserDetail.UserDetailDto userDetailDto;
    UserPrincipal userPrincipal;


    List<UserDetail.UserDetailDto> userDetailDtoList;


    @BeforeEach
    public void setUp() {

        user = User.builder()
                .password("Password%123")
                .id(1L).provider(AuthProvider.local)
                .email("test@mail.com").emailVerified(true).build();

        userPrincipal = UserPrincipal.create(user);

        userDetail = UserDetail.builder()
                .id(1L)
                .user(user)
                .age(15)
                .city("Gdynia")
                .gender(Gender.FEMALE).build();


        userDetailDto = UserDetail.UserDetailDto.builder().city("Gdynia").build();

        userDetailDtoList = new ArrayList<>();
    }

    @Disabled
    @Test
    public void shouldReturnUsersDetails() {
        PaginationInfoRequest paginationInfoRequest = new PaginationInfoRequest(0,10);
        PageModel pageModel = new PageModel(0,10,1,1, true);

        userDetailDtoList.add(userDetailDto);

        PagedResponseDto pagedResponseDto = PagedResponseDtoBuilder.create().listDto(userDetailDtoList).entityPage(pageModel).build();

        Mockito.when(userDetailService.searchUser("search=city:Gdynia",paginationInfoRequest)).thenReturn(pagedResponseDto);

        assertEquals(userDetailService.searchUser("search=city:Gdynia",paginationInfoRequest), pagedResponseDto);


    }

    @Test
    public void shouldAddUserDetail() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(userDetailRepository.save(ArgumentMatchers.any(UserDetail.class))).thenReturn(userDetail);

        userDetailService.addUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(userDetailService.addUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {

        Exception exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userDetailService.addUserDetail(userDetail, userPrincipal),
                "Expected doThing() to throw, but it didn't");

        assertEquals("UserProfile not found with ID : '"+ userPrincipal.getUsername()+"'", exception.getMessage());
    }

    @Test
    public void shouldUpdateUserDetail() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));
        Mockito.when(userDetailRepository.save(ArgumentMatchers.any(UserDetail.class))).thenReturn(userDetail);

        userDetailService.updateUserDetail(userDetail, userPrincipal);
        verify(userDetailRepository, times(1)).save(userDetail);

        assertEquals(userDetailService.updateUserDetail(userDetail, userPrincipal).getStatusCode(), HttpStatus.OK);

    }

    @Test
    public void shouldDeleteUserDetail() {

        Mockito.when(userRepository.findByIdAndEmail(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(Optional.of(user));

        userDetailService.deleteUserDetail(userDetail.getId(), userPrincipal);
        verify(userDetailRepository, times(1)).deleteById(userDetail.getId());

        assertEquals(userDetailService.deleteUserDetail(userDetail.getId(), userPrincipal).getStatusCode(), HttpStatus.NO_CONTENT);
    }
}
