package kr.co.kosmo.project_back.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.kosmo.project_back.user.dto.AddressDto;
import kr.co.kosmo.project_back.user.dto.SignUpDto;
import kr.co.kosmo.project_back.user.mapper.LoginMapper;
import kr.co.kosmo.project_back.user.mapper.SelectedCategoryMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final LoginMapper loginMapper;
    private final SelectedCategoryMapper selectedCategoryMapper;

    @Transactional
    public Integer join(SignUpDto dto, HttpServletRequest request) {
        // 중복 체크
        if (loginMapper.countByLoginId(dto.getLoginId()) > 0) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        // 회원가입 처리
        Integer result = loginMapper.insertUser(dto);
        
        if (result == null || result <= 0) {
            throw new IllegalStateException("회원가입에 실패했습니다.");
        }

        // 주소 정보를 Address 테이블에 저장
        if (result > 0 && dto.getZipcode() != null && dto.getAddress() != null && dto.getAddressDetail() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setUserId(result); // insertUser가 반환한 userId 사용
            addressDto.setRecipient(dto.getName()); // 수령인은 회원 이름
            addressDto.setPostcode(dto.getZipcode());
            addressDto.setAddress(dto.getAddress());
            addressDto.setDetailAddress(dto.getAddressDetail());
            addressDto.setRecipientPhone(dto.getPhone()); // 수령인 전화번호는 회원 전화번호
            addressDto.setIsDefault(true); // 회원가입 시 첫 주소이므로 기본 주소로 설정
            loginMapper.insertAddress(addressDto);
        }
        
        // 관심 카테고리 등록
        if (result > 0 && dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
            selectedCategoryMapper.insertUserCategories(result, dto.getCategoryId());
        }

        return result;
    }

    public boolean isIdDuplicate(String loginId) {
        return loginMapper.countByLoginId(loginId) > 0;
    }
}

