package kr.co.kosmo.project_back.user.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.kosmo.project_back.address.dto.AddressDto;
import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import kr.co.kosmo.project_back.cart.mapper.CartMapper;
import kr.co.kosmo.project_back.question.mapper.QuestionImageMapper;
import kr.co.kosmo.project_back.question.mapper.QuestionMapper;
import kr.co.kosmo.project_back.recentproduct.mapper.RecentProductMapper;
import kr.co.kosmo.project_back.review.mapper.ReviewImageMapper;
import kr.co.kosmo.project_back.review.mapper.ReviewMapper;
import kr.co.kosmo.project_back.user.dto.UserDto;
import kr.co.kosmo.project_back.user.dto.UserJoinDto;
import kr.co.kosmo.project_back.user.dto.UserPasswordChangeRequestDto;
import kr.co.kosmo.project_back.user.dto.UserUpdateRequestDto;
import kr.co.kosmo.project_back.user.mapper.UserMapper;
import kr.co.kosmo.project_back.user.mapper.UserSelectedCategoryMapper;
import kr.co.kosmo.project_back.wishlist.mapper.WishlistMapper;

@Service
@RequiredArgsConstructor
public class UserService {
   private final UserMapper userMapper;
   private final AddressMapper addressMapper;
   private final ReviewMapper reviewMapper;
   private final ReviewImageMapper reviewImageMapper;
   private final QuestionMapper questionMapper;
   private final QuestionImageMapper questionImageMapper;
   private final WishlistMapper wishlistMapper;
   private final CartMapper cartMapper;
   private final RecentProductMapper recentProductMapper;
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

   @Transactional
   public void updateUserInfo(Integer userId, UserUpdateRequestDto dto) {
        // 이름 / 전화번호 수정
        if(dto.getUserName() != null || dto.getPhone() != null) {
            userMapper.updateUserInfo(userId, dto);
        }
        // 관심분야 수정
        if(dto.getCategory() != null) {
            if(dto.getCategory().size() > 5) {
                throw new IllegalArgumentException("관심분야는 최대 5개까지 선택 가능합니다.");
            }
            userSelectedCategoryMapper.deleteCategoriesByUser(userId);
            if(!dto.getCategory().isEmpty()) {
                userSelectedCategoryMapper.insertUserCategories(
                    userId, dto.getCategory());
            }
        }
    }
    // 비밀번호 변경
   @Transactional
   public void changePassword(
         Integer userId, UserPasswordChangeRequestDto dto) {
    // 사용자 조회
    UserDto user = userMapper.selectById(userId);
    if(user == null) {
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
    }
    // 현재 비번 확인
    if(!user.getPassword().equals(dto.getCurrentPassword())) {
        throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
    }
    // 새 비번 업뎃
    userMapper.updatePasswordByUserId(userId, dto.getNewPassword());
   }
   // 회원 탈퇴
   @Transactional // 회원 탈퇴, 회원 가입 같은 여러 기능이 섞여있을 때 사용
   public void withdraw(Integer userId) {
    // 리뷰 삭제
    // 리뷰 아이디 목록 조회
    List<Long> reviewIds = reviewMapper.findReviewIdsByUserId(userId);
    // 리뷰 이미지 삭제
    for(Long reviewId : reviewIds) {
        reviewImageMapper.deleteByReviewId(reviewId);
    }
    // 리뷰 삭제
    reviewMapper.deleteByUserId(userId);

    // 문의 삭제
    // 사용자가 작성한 문의 아이디 목록 조회
    List<Long> questionIds = questionMapper.findQuestionIdsByUserId(userId);
    // 문의 이미지 삭제
    for(Long questionId : questionIds) {
        questionImageMapper.deleteByQuestionId(questionId);
    }
    // 문의 삭제
    questionMapper.deleteByUserId(userId);

    // 찜 삭제
    wishlistMapper.deleteByUserId(userId);
    // 장바구니 삭제
    cartMapper.deleteByUserId(userId);
    // 최근 본 상품 삭제
    recentProductMapper.deleteByUserId(userId);
    // 주소 삭제
    addressMapper.deleteByUserId(userId);
    // 관심분야 삭제
    userSelectedCategoryMapper.deleteCategoriesByUser(userId);

    // 회원 삭제
    userMapper.deleteUser(userId);
   }
}
