package kr.co.kosmo.project_back.admin.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminUserResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.UserSearchDto;
import kr.co.kosmo.project_back.admin.mapper.AdminUserMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {
    private final AdminUserMapper userMapper;

    public PageResponseDto<AdminUserResponseDto> getUserList(UserSearchDto dto) {
        Integer totalCount = userMapper.countUserList(dto);
        Integer totalPage = (int) Math.ceil((double) totalCount / dto.getSize());
        
        List<AdminUserResponseDto> userList = userMapper.findUserList(dto);
        
        PageResponseDto<AdminUserResponseDto> response = new PageResponseDto<>();
        response.setList(userList);
        response.setTotalPage(totalPage);
        response.setCurrentPage(dto.getPage());
        
        return response;
    }

    public AdminUserResponseDto getUser(Integer id) {
        return userMapper.findById(id);
    }
}

