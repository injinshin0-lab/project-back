package kr.co.kosmo.project_back.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private Integer addressId;
    private Integer userId;
    private String recipient;
    private String postcode;
    private String address;
    private String detailAddress;
    private String recipientPhone;
    private Boolean isDefault;
}

