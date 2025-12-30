package kr.co.kosmo.project_back.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.user.vo.UserVO;

@Mapper
public interface UserMapper {
    // 아이디 중복 체크
    int existsByLoginId(String loginId);

    // 회원 저장
    void insertUser(UserVO user);  
    
    // 로그인용 조회
    UserVO selectByLoginId(String loginId);
    UserVO selectById(Integer id);
}
