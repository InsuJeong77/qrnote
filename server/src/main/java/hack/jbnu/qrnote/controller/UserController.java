package hack.jbnu.qrnote.controller;

import hack.jbnu.qrnote.api.ApiMessage;
import hack.jbnu.qrnote.domain.User;
import hack.jbnu.qrnote.dto.LoginDTO;
import hack.jbnu.qrnote.service.UserService;
import hack.jbnu.qrnote.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SmartValidator smartValidator;
    private final UserValidator userValidator;

    @PostMapping("/signup")
    public ResponseEntity<ApiMessage> signup(@RequestBody User user, BindingResult result) {
        smartValidator.validate(user, result);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            //에러메시지
            return ResponseEntity.ok(ApiMessage.builder().data(false).message("에러 메시지").build());
        }
        userService.create(user);
        //성공메세지
        return ResponseEntity.ok(ApiMessage.builder().data(true).message("성공 메시지").build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiMessage> loginProcess(@RequestBody User user) {
        LoginDTO loginInfo = userService.login(user.getLoginId(), user.getPassword());
        return ResponseEntity.ok(ApiMessage.builder().data(loginInfo).message(loginInfo.getMessage()).build());
    }

    @PostMapping("/token-login")
    public ResponseEntity<ApiMessage> tokenLoginProcess(@RequestHeader("token") String token) {

        LoginDTO loginInfo = userService.tokenLogin(token);
        return ResponseEntity.ok(ApiMessage.builder().data(loginInfo).message(loginInfo.getMessage()).build());
    }
}
