package kr.co.kosmo.project_back.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.DashboardDto;

@Mapper
public interface AdminDashboardMapper {
    DashboardDto getTotalCounts();
}







