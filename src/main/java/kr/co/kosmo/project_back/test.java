package kr.co.kosmo.project_back;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping("/")
    public String home() {
        return "화면 나와!!!";
    }

}
