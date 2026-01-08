package kr.co.kosmo.project_back.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.SignUpDto;
import kr.co.kosmo.project_back.user.dto.EmailCodeDto;
import kr.co.kosmo.project_back.user.dto.AddressDto;
import kr.co.kosmo.project_back.user.dto.UserResponseDto;

@Mapper
public interface LoginMapper {
    UserResponseDto findByAutoLoginToken(String token);
    UserResponseDto findByLoginId(String loginId);
    UserResponseDto findById(Integer id);
    Integer countByLoginId(String loginId);
    Integer insertAuthCodeByEmail(EmailCodeDto dto);
    String findAuthCodeByEmail(String email);
    Integer insertUser(SignUpDto dto);
    Integer insertAddress(AddressDto dto);
    Integer updatePassword(@Param("loginId") String loginId, @Param("password") String password);
    Integer updatePasswordByUserId(@Param("userId") Integer userId, @Param("newPassword") String newPassword);
    UserResponseDto findByNameAndEmail(@Param("name") String name, @Param("email") String email);
    UserResponseDto findByLoginIdAndEmail(@Param("loginId") String loginId, @Param("email") String email);
    Integer updateLastLoginAt(@Param("userId") Integer userId);
}
