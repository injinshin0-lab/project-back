package kr.co.kosmo.project_back.address.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.kosmo.project_back.address.vo.AddressVO;

@Mapper
public interface AddressMapper {
    void insertAddress(AddressVO address);
}
