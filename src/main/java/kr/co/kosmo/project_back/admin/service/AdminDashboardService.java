package kr.co.kosmo.project_back.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.DashboardDto;
import kr.co.kosmo.project_back.admin.mapper.AdminDashboardMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashboardService {
    private final AdminDashboardMapper dashboardMapper;

    public DashboardDto getDashboard() {
        return dashboardMapper.getTotalCounts();
    }
}







