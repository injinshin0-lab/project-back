package kr.co.kosmo.project_back.address.mapper;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param; // 추가
import kr.co.kosmo.project_back.address.dto.AddressDto;
import java.util.List; // 추가

@Mapper
public interface AddressMapper {
    // 기존 유지
    void insertAddress(AddressDto address);


    // 목록 조회를 위해 추가 (프론트 연동 필수 기능)
    List<AddressDto> selectAddressesByUserId(@Param("userId") Integer userId);
}