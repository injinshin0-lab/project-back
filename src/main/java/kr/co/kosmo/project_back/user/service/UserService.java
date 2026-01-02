package kr.co.kosmo.project_back.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import kr.co.kosmo.project_back.address.vo.AddressVO;
import kr.co.kosmo.project_back.user.dto.UserJoinDto;
import kr.co.kosmo.project_back.user.mapper.UserMapper;
import kr.co.kosmo.project_back.user.vo.UserVO;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserMapper userMapper;
   private final PasswordEncoder passwordEncoder;
   private final AddressMapper addressMapper;

   public Integer join(UserJoinDto dto, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 이메일 인증 여부 확인
        if(session == null) {
            throw new IllegalStateException("이메일 인증이 필요합니다.");
        }
        Boolean emailAuthSuccess = (Boolean) session.getAttribute("EMAIL_AUTH_SUCCESS");
        String authEmail = (String) session.getAttribute("EMAIL_AUTH_EMAIL");

        if(emailAuthSuccess == null || !emailAuthSuccess) {
            throw new IllegalStateException("이메일 인증이 필요합니다.");
        }
        if(authEmail == null || !authEmail.equals(dto.getEmail())) {
            throw new IllegalStateException("이메일을 확인해주세요.");
        }

        // // 아이디 중복 체크 시작
        if( userMapper.existsByLoginId(dto.getUserId()) > 0 ) {
            throw new IllegalStateException( "이미 존재하는 아이디입니다." );
        }
        // DTO -> VO 변환
        UserVO user = new UserVO();
        user.setLoginId(dto.getUserId());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUserName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole("USER");

        userMapper.insertUser(user);   // DB 저장해 -> user_id 생성
        Integer userId = user.getId();

        AddressVO address = new AddressVO();
        address.setUserId(userId);
        address.setRecipient(dto.getName());
        address.setPostcode(dto.getZipcode());
        address.setAddress(dto.getAddress());
        address.setDetailAddress(dto.getAddressDetail());
        address.setRecipientPhone(dto.getPhone());
        address.setIsDefault(1);

        addressMapper.insertAddress(address);

        return userId;
   }
   public boolean isIdDuplicate(String loginId) {
       return userMapper.existsByLoginId(loginId) > 0;
   }
}
