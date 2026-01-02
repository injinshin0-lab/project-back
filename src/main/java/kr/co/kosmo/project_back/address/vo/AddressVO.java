package kr.co.kosmo.project_back.address.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressVO {
    private Integer id;
    private Integer userId;
    private String recipient;
    private String postcode;
    private String address;
    private String detailAddress;
    private String recipientPhone;
    private Integer isDefault;
}
