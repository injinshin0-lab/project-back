package kr.co.kosmo.project_back.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String recipient;
    private String address;
    private String datail;
    private String postcode;
    private String recipientPhone;
}
