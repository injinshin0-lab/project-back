package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminUserResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.UserSearchDto;
import kr.co.kosmo.project_back.admin.service.AdminUserManagementService;

@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
public class UserManagementController {
    private final AdminUserManagementService userManagementService;

    @GetMapping
    public ResponseEntity<PageResponseDto<AdminUserResponseDto>> getUserList(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String keywordType,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        
        UserSearchDto dto = new UserSearchDto();
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setKeywordType(keywordType);
        dto.setKeyword(keyword);
        dto.setPage(page);
        dto.setSize(size);
        
        return ResponseEntity.ok(userManagementService.getUserList(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserResponseDto> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userManagementService.getUser(userId));
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<Integer> updateUserStatus(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> request) {
        String status = request.get("status");
        // TODO: 회원 상태 변경 로직 구현 필요 (정지/탈퇴)
        return ResponseEntity.ok(1);
    }
}


