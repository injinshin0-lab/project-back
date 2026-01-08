package kr.co.kosmo.project_back.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.AddressDto;
import kr.co.kosmo.project_back.user.mapper.AddressMapper;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressMapper addressMapper;

    public List<AddressDto> getAddressList(Integer userId) {
        return addressMapper.findByUserId(userId);
    }

    public AddressDto getAddress(Integer addressId) {
        return addressMapper.findById(addressId);
    }

    @Transactional
    public Integer insertAddress(AddressDto dto) {
        // 기본 주소로 설정하는 경우, 기존 기본 주소 해제
        if (dto.getIsDefault() != null && dto.getIsDefault()) {
            addressMapper.updateDefaultAddress(dto.getUserId());
        }
        Integer result = addressMapper.insertAddress(dto);
        // 기본 주소로 설정하는 경우, 방금 추가한 주소를 기본 주소로 설정
        if (result > 0 && dto.getIsDefault() != null && dto.getIsDefault() && dto.getAddressId() != null) {
            addressMapper.setDefaultAddress(dto.getUserId(), dto.getAddressId());
        }
        return result;
    }

    @Transactional
    public Integer updateAddress(AddressDto dto) {
        // 기본 주소로 설정하는 경우, 기존 기본 주소 해제 후 선택한 주소를 기본으로 설정
        if (dto.getIsDefault() != null && dto.getIsDefault()) {
            addressMapper.updateDefaultAddress(dto.getUserId());
            addressMapper.setDefaultAddress(dto.getUserId(), dto.getAddressId());
        }
        return addressMapper.updateAddress(dto);
    }

    public Integer deleteAddress(Integer addressId, Integer userId) {
        return addressMapper.deleteAddress(addressId, userId);
    }

    @Transactional
    public Integer setDefaultAddress(Integer addressId, Integer userId) {
        // 모든 주소를 기본 주소 해제
        addressMapper.updateDefaultAddress(userId);
        // 선택한 주소를 기본 주소로 설정
        return addressMapper.setDefaultAddress(userId, addressId);
    }
}

