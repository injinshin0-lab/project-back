package kr.co.kosmo.project_back.user.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.co.kosmo.project_back.user.dto.AddressDto;

@Mapper
public interface AddressMapper {
    List<AddressDto> findByUserId(@Param("userId") Integer userId);
    AddressDto findById(@Param("addressId") Integer addressId);
    Integer insertAddress(AddressDto dto);
    Integer updateAddress(AddressDto dto);
    Integer deleteAddress(@Param("addressId") Integer addressId, @Param("userId") Integer userId);
    Integer updateDefaultAddress(@Param("userId") Integer userId);
    Integer setDefaultAddress(@Param("userId") Integer userId, @Param("addressId") Integer addressId);
}

