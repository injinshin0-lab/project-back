package kr.co.kosmo.project_back.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private Long totalUserCount;
    private Long totalProductCount;
    private Long totalOrderCount;
    private Long totalSalesAmount;
}
