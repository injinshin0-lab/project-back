package kr.co.kosmo.project_back.address.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.kosmo.project_back.address.dto.AddressDto;

@Mapper
public interface AddressMapper {
    void insertAddress(AddressDto address);
    List<AddressDto> selectAddressByUserId(@Param("userId")Integer userId);
}

