package kr.co.kosmo.project_back.address.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.kosmo.project_back.address.dto.AddressDto;
import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressMapper addressMapper;
    // 조회
    public List<AddressDto> getAddresses(Integer userId) {
        return addressMapper.selectAddressesByUserId(userId);
    }
    // 추가
    public void addAddress(AddressDto dto) {
        if(dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            addressMapper.clearDefaultAddress(dto.getUserId());
        }
        addressMapper.insertAddress(dto);
    }
    // 수정
    public void updateAddress(AddressDto dto) {
        if(dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            addressMapper.clearDefaultAddress(dto.getUserId());
        }
        addressMapper.updateAddress(dto);
    }
    // 삭제
    public void deleteAddress(Integer userId, Integer addressId) {
        addressMapper.deleteAddress(userId, addressId);
    }
}
