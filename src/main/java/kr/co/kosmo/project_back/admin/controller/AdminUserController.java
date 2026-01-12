package kr.co.kosmo.project_back.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import kr.co.kosmo.project_back.admin.dto.AdminUserResponseDto;
import kr.co.kosmo.project_back.admin.dto.PageResponseDto;
import kr.co.kosmo.project_back.admin.dto.UserSearchDto;
import kr.co.kosmo.project_back.admin.service.AdminUserService;
import kr.co.kosmo.project_back.user.service.AlarmService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserService userService;
    private final AlarmService alarmService;

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
        
        return ResponseEntity.ok(userService.getUserList(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserResponseDto> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PostMapping("/{userId}/alarm")
    public ResponseEntity<Integer> sendAlarm(
            @PathVariable Integer userId,
            @RequestBody AlarmRequestDto request) {
        return ResponseEntity.ok(alarmService.insertAdminAlarm(userId, request.getMessage()));
    }

    public static class AlarmRequestDto {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}

