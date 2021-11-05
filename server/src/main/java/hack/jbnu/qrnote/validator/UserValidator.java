package hack.jbnu.qrnote.validator;

import hack.jbnu.qrnote.domain.User;
import hack.jbnu.qrnote.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserService userService;
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByLoginId(user.getLoginId()) != null) {
            errors.rejectValue("loginId", null, "이미 존재하는 사용자입니다.");
        }
    }
}
