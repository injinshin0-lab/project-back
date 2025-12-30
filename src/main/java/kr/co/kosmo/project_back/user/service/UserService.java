package kr.co.kosmo.project_back.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.user.dto.UserJoinDto;
import kr.co.kosmo.project_back.user.mapper.UserMapper;
import kr.co.kosmo.project_back.user.vo.UserVO;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;
   // private final BCryptPasswordEncoder passwordEncoder;

   public Integer join(UserJoinDto dto) {

        // // 아이디 중복 체크 시작
        if( userMapper.existsByLoginId(dto.getUserId()) == 1 ) {
            throw new IllegalStateException( "이미 존재하는 아이디입니다." );
        }
        // DTO -> VO 변환
        UserVO user = new UserVO();
        user.setLoginId(dto.getUserId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole("USER");
        user.setZipcode(dto.getZipcode());
        user.setAddress(dto.getAddress());
        user.setAddressDetail(dto.getAddressDetail());

        userMapper.insertUser(user);   // DB 저장해 -> user_id 생성
        return user.getId();
   }
   public boolean isIdDuplicate(String loginId) {
       return userMapper.existsByLoginId(loginId) == 1;
   }
   
}
