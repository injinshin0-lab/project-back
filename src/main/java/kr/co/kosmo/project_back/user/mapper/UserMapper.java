package kr.co.kosmo.project_back.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.alarm.dto.AlarmSettingDto;
import kr.co.kosmo.project_back.user.dto.UserDto;
import kr.co.kosmo.project_back.user.dto.UserJoinDto;
import kr.co.kosmo.project_back.user.dto.UserUpdateRequestDto;

@Mapper
public interface UserMapper {
    // 아이디 중복 체크
    int existsByLoginId(@Param("loginId") String loginId);

    // 회원 저장
    void insertUser(UserJoinDto dto);  
    
    // 로그인용 조회
    UserDto selectByLoginId(@Param("loginId") String loginId);
    UserDto selectById(@Param("id") Integer id);

    // 아이디 찾기
    UserDto findByNameAndEmail(
        @Param("name") String name,
        @Param("email") String email
    );

    // 바뀐 비밀번호 저장
    void updatePasswordByEmail(
        @Param("email") String email,
        @Param("password") String password
    );
    // 마지막 로그인 시간 업뎃
    void updateLastLoginAt(@Param("userId") Integer userId);

    // 마이페이지 - 회원 정보 수정
    void updateUserInfo(
        @Param("userId") Integer userId,
        @Param("dto") UserUpdateRequestDto dto
    );
    // 마이페이지 - 비밀번호 변경
    void updatePasswordByUserId(
        @Param("userId") Integer userId,
        @Param("password") String password 
    );

    // 회원 탈퇴
    void deleteUser(@Param("userId") Integer userId);
}
