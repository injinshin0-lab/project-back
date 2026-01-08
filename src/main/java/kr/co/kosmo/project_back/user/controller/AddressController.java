package kr.co.kosmo.project_back.user.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.user.dto.AddressDto;
import kr.co.kosmo.project_back.user.service.AddressService;

@RestController
@RequestMapping("/api/v1/user/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAddressList(@RequestParam Integer userId) {
        return ResponseEntity.ok(addressService.getAddressList(userId));
    }

    @PostMapping
    public ResponseEntity<Integer> insertAddress(@RequestBody AddressDto dto) {
        Integer result = addressService.insertAddress(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping
    public ResponseEntity<Integer> updateAddress(@RequestBody AddressDto dto) {
        Integer result = addressService.updateAddress(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteAddress(
            @RequestParam Integer addressId,
            @RequestParam Integer userId) {
        Integer result = addressService.deleteAddress(addressId, userId);
        return ResponseEntity.ok(result);
    }
}



