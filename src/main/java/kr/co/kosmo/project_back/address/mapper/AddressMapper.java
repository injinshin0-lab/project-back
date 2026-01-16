package kr.co.kosmo.project_back.address.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param; 
import kr.co.kosmo.project_back.address.dto.AddressDto;
import java.util.List; 

@Mapper
public interface AddressMapper {

    // 주소 목록 조회
    List<AddressDto> selectAddressesByUserId(@Param("userId") Integer userId);
    // 주소 추가
    void insertAddress(AddressDto address);
    // 주소 수정
    void updateAddress(AddressDto dto);
    // 주소 삭제
    void deleteAddress(
        @Param("userId") Integer userId,
        @Param("addressId") Integer addressId
    );
    // 기본 배송지 해제
    void clearDefaultAddress(@Param("userId") Integer userId);

    // 회원 탈퇴
    void deleteByUserId(@Param("userId") Integer userId);
}