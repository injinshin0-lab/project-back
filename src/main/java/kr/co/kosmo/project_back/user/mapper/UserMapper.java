package kr.co.kosmo.project_back.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.user.vo.UserVO;

@Mapper
public interface UserMapper {
    // 아이디 중복 체크
    int existsByLoginId(@Param("loginId") String loginId);

    // 회원 저장
    void insertUser(UserVO user);  
    
    // 로그인용 조회
    UserVO selectByLoginId(@Param("loginId") String loginId);
    UserVO selectById(@Param("id") Integer id);

    // 아이디 찾기
    UserVO findByNameAndEmail(
        @Param("name") String name,
        @Param("email") String email
    );

    // 바뀐 비밀번호 저장
    void updatePasswordByEmail(
        @Param("email") String email,
        @Param("password") String password
    );
}
