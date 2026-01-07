package kr.co.kosmo.project_back.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminUserResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.UserSearchDto;
import kr.co.kosmo.project_back.admin.mapper.AdminUserManagementMapper;

@Service
@RequiredArgsConstructor
public class AdminUserManagementService {
    private final AdminUserManagementMapper userManagementMapper;

    public PageResponseDto<AdminUserResponseDto> getUserList(UserSearchDto dto) {
        Integer totalCount = userManagementMapper.countUserList(dto);
        Integer totalPage = (int) Math.ceil((double) totalCount / dto.getSize());
        
        List<AdminUserResponseDto> userList = userManagementMapper.findUserList(dto);
        
        PageResponseDto<AdminUserResponseDto> response = new PageResponseDto<>();
        response.setList(userList);
        response.setTotalPage(totalPage);
        response.setCurrentPage(dto.getPage());
        
        return response;
    }

    public AdminUserResponseDto getUser(Integer id) {
        return userManagementMapper.findById(id);
    }
}







