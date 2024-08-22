package Hoseo.GraduationProject.Member.Service;

import Hoseo.GraduationProject.API.Major.Domain.Major;
import Hoseo.GraduationProject.API.Major.Service.AdminMajorService;
import Hoseo.GraduationProject.Exception.BusinessLogicException;
import Hoseo.GraduationProject.Member.DTO.ChangePwDTO;
import Hoseo.GraduationProject.Member.DTO.FindUserIdDTO;
import Hoseo.GraduationProject.Member.DTO.JoinDTO;
import Hoseo.GraduationProject.Member.DTO.Response.ResponseProfessorDTO;
import Hoseo.GraduationProject.Member.DTO.Response.ResponseProfessorListDTO;
import Hoseo.GraduationProject.Member.DTO.Role;
import Hoseo.GraduationProject.Member.Domain.Member;
import Hoseo.GraduationProject.Member.ExceptionType.MemberExceptionType;
import Hoseo.GraduationProject.Member.Repository.MemberRepository;
import Hoseo.GraduationProject.Member.Repository.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RedisRepository redisRepository;

    @Mock
    private AdminMajorService adminMajorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 시 성공적인 경우 회원 정보를 저장하고 적절한 결과를 반환한다")
    void joinProcess_whenSuccessful_savesMemberAndReturnsResponse() {
        // Given
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setId("newId");
        joinDTO.setRole(Role.STUDENT);
        joinDTO.setPassword("password");

        Member member = Member.builder()
                .id(joinDTO.getId())
                .role("ROLE_" + joinDTO.getRole())
                .password("encodedPassword")  // Encoded password
                .build();

        when(bCryptPasswordEncoder.encode(joinDTO.getPassword())).thenReturn("encodedPassword");
        when(memberRepository.findById(joinDTO.getId())).thenReturn(Optional.empty());  // Member does not exist
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // When
        memberService.joinProcess(joinDTO);

        // Then
        verify(memberRepository).save(argThat(savedMember ->
                savedMember.getId().equals(member.getId()) &&
                        savedMember.getRole().equals(member.getRole()) &&
                        savedMember.getPassword().equals(member.getPassword())
        ));
    }


    @Test
    @DisplayName("가입 시 회원이 존재하면 예외를 발생시킨다")
    void joinProcess_whenMemberExists_throwsException() {
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setId("existingId");
        joinDTO.setRole(Role.STUDENT);

        when(memberRepository.findById(joinDTO.getId())).thenReturn(Optional.of(new Member()));

        BusinessLogicException thrown = assertThrows(BusinessLogicException.class, () -> {
            memberService.joinProcess(joinDTO);
        });

        assertEquals(MemberExceptionType.MEMBER_CONFLICT, thrown.getExceptionType());
    }

    @Test
    @DisplayName("가입 시 관리자 역할로 설정하면 예외를 발생시킨다")
    void joinProcess_whenAdminRole_throwsException() {
        JoinDTO joinDTO = new JoinDTO();
        joinDTO.setId("newId");
        joinDTO.setRole(Role.ADMIN);

        BusinessLogicException thrown = assertThrows(BusinessLogicException.class, () -> {
            memberService.joinProcess(joinDTO);
        });

        assertEquals(MemberExceptionType.NOT_ACCESS_LEVEL, thrown.getExceptionType());
    }

    @Test
    @DisplayName("회원 ID 찾기 시 회원이 존재하면 ID를 반환한다")
    void findId_whenMemberExists_returnsId() {
        FindUserIdDTO findUserIdDTO = new FindUserIdDTO();
        findUserIdDTO.setEmail("test@example.com");
        findUserIdDTO.setName("Test User");

        Member member = Member.builder()
                .id("123123")
                .role(String.valueOf(Role.STUDENT))
                .email("test@example.com")
                .major(new Major())
                .password("asdqwe12@@")
                .name("Test User")
                .harnessingResource(1L)
                .entrepreneurship(1L)
                .creativeThinking(1L)
                .teamwork(1L)
                .build();

        when(memberRepository.findByEmailAndName(findUserIdDTO.getEmail(), findUserIdDTO.getName())).thenReturn(member);

        String id = memberService.findId(findUserIdDTO);

        assertEquals("123123", id);
    }

    @Test
    @DisplayName("회원 ID 찾기 시 회원이 존재하지 않으면 예외를 발생시킨다")
    void findId_whenMemberDoesNotExist_throwsException() {
        FindUserIdDTO findUserIdDTO = new FindUserIdDTO();
        findUserIdDTO.setEmail("test@example.com");
        findUserIdDTO.setName("Test User");

        when(memberRepository.findByEmailAndName(findUserIdDTO.getEmail(), findUserIdDTO.getName())).thenReturn(null);

        BusinessLogicException thrown = assertThrows(BusinessLogicException.class, () -> {
            memberService.findId(findUserIdDTO);
        });

        assertEquals(MemberExceptionType.NONE_MEMBER, thrown.getExceptionType());
    }

    @Test
    @DisplayName("비밀번호 변경 시 비밀번호가 일치하면 비밀번호를 업데이트한다")
    void changePassword_whenPasswordsMatch_updatesPassword() {
        ChangePwDTO changePwDTO = new ChangePwDTO();
        changePwDTO.setId("memberId");
        changePwDTO.setPassword("newPassword");
        changePwDTO.setCheckPw("newPassword");

        Member member = new Member();
        when(memberRepository.findById(changePwDTO.getId())).thenReturn(Optional.of(member));
        when(bCryptPasswordEncoder.encode(changePwDTO.getPassword())).thenReturn("encodedPassword");

        memberService.changePassword(changePwDTO);

        verify(memberRepository).findById(changePwDTO.getId());
        assertEquals("encodedPassword", member.getPassword());
    }

    @Test
    @DisplayName("교수 목록 조회 시 교수 목록을 반환한다")
    void getProfessorList_returnsProfessorList() {
        Member member1 = Member.builder()
                .id("prof1")
                .role(String.valueOf(Role.PROFESSOR))
                .email("prof1@example.com")
                .major(new Major())
                .password("password1")
                .name("Professor 1")
                .build();

        Member member2 = Member.builder()
                .id("prof2")
                .role(String.valueOf(Role.PROFESSOR))
                .email("prof2@example.com")
                .major(new Major())
                .password("password2")
                .name("Professor 2")
                .build();

        when(memberRepository.findByRole("ROLE_PROFESSOR")).thenReturn(List.of(member1, member2));

        ResponseProfessorListDTO responseProfessorListDTO = memberService.getProfessorList();

        List<ResponseProfessorDTO> professorDTOList = responseProfessorListDTO.getProfessorDTOList();
        assertEquals(2, professorDTOList.size());
        assertEquals("prof1", professorDTOList.get(0).getId());
        assertEquals("Professor 1", professorDTOList.get(0).getName());
    }
}
