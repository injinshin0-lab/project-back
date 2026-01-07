package kr.co.kosmo.project_back.address.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.address.dto.AddressDto;

@Mapper
public interface AddressMapper {
    void insertAddress(AddressDto address);
}
