package kr.co.kosmo.project_back.address.controller;

import kr.co.kosmo.project_back.address.dto.AddressDto;
import kr.co.kosmo.project_back.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    // 주소록 조회
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<?> getAddresses(
        @PathVariable Integer userId,
        HttpSession session
    ) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null || !loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("본인 주소만 조회할 수 있습니다.");
        }
        return ResponseEntity.ok(addressService.getAddresses(userId));
    }

    // 주소록 추가
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<?> addAddresses(
        @PathVariable Integer userId, 
        @RequestBody AddressDto dto,
        HttpSession session
    ) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null || !loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("본인 주소만 추가할 수 있습니다.");
        }
        dto.setUserId(userId);
        if(dto.getIsDefault() == null) dto.setIsDefault(0);
        addressService.addAddress(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("주소가 추가되었습니다.");
    }  

    // 주소록 수정
    @PatchMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<?> updateAddresses(
        @PathVariable Integer userId,
        @PathVariable Integer addressId,
        @RequestBody AddressDto dto,
        HttpSession session
    ) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null || !loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("본인 주소만 수정할 수 있습니다.");
        }
        dto.setUserId(userId);
        dto.setId(addressId);
        addressService.updateAddress(dto);

        return ResponseEntity.ok("주소가 수정되었습니다.");
    }

    // 주소록 삭제
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<?> deleteAddresses(
        @PathVariable Integer userId,
        @PathVariable Integer addressId,
        HttpSession session
    ) {
        Integer loginUserId = (Integer) session.getAttribute("LOGIN_USER");
        if(loginUserId == null || !loginUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("본인 주소만 삭제할 수 있습니다.");
        }
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.ok("주소가 삭제되었습니다.");
    }
}