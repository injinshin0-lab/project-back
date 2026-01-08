package kr.co.kosmo.project_back.address.controller;

import kr.co.kosmo.project_back.address.dto.AddressDto;
import kr.co.kosmo.project_back.address.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressController {

    private final AddressMapper addressMapper;

    /**
     * 주소록 목록 조회
     * 프론트엔드 getAddressesApi와 연동
     */
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressDto>> getAddresses(@PathVariable("userId") Integer userId) {
        // Mapper에 select 기능이 없다면 추가가 필요합니다.
        List<AddressDto> list = addressMapper.selectAddressesByUserId(userId);
        return ResponseEntity.ok(list);
    }

    /**
     * 주소 추가 (회원가입 이후 추가 시 사용)
     * 프론트엔드 createAddressApi와 연동
     */
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<String> addAddress(
            @PathVariable("userId") Integer userId, 
            @RequestBody AddressDto dto) {
        
        dto.setUserId(userId);
        // 기본값 설정 (필요시)
        if (dto.getIsDefault() == null) dto.setIsDefault(0);
        
        addressMapper.insertAddress(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("주소가 추가되었습니다.");
    }
}