package kr.co.kosmo.project_back.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.address.dto.AddressDto;
import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import kr.co.kosmo.project_back.user.dto.UserJoinDto;
import kr.co.kosmo.project_back.user.mapper.UserMapper;
import kr.co.kosmo.project_back.user.mapper.UserSelectedCategoryMapper;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserMapper userMapper;
   private final AddressMapper addressMapper;
   private final UserSelectedCategoryMapper userSelectedCategoryMapper;

   @Transactional       // 회원저장 + 관심분야 저장 + 주소저장 = 하나의 회원가입으로 묶음 
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
        if( userMapper.existsByLoginId(dto.getLoginId()) > 0 ) {
            throw new IllegalStateException( "이미 존재하는 아이디입니다." );
        }
    
        dto.setPassword(dto.getPassword());
        userMapper.insertUser(dto);
        Integer userId = dto.getId();

        userSelectedCategoryMapper.insertUserCategories(userId, dto.getCategories());

        AddressDto address = new AddressDto();
        address.setUserId(userId);
        address.setRecipient(dto.getName());
        address.setPostcode(dto.getPostcode());
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
