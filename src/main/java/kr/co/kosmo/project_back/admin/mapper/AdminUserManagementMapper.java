package kr.co.kosmo.project_back.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import kr.co.kosmo.project_back.admin.dto.AdminUserResponseDto;
import kr.co.kosmo.project_back.admin.dto.UserSearchDto;

@Mapper
public interface AdminUserManagementMapper {
    List<AdminUserResponseDto> findUserList(UserSearchDto dto);
    Integer countUserList(UserSearchDto dto);
    AdminUserResponseDto findById(Integer id);
}







