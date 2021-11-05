package hack.jbnu.qrnote.service;

import hack.jbnu.qrnote.domain.User;
import hack.jbnu.qrnote.dto.LoginDTO;
import hack.jbnu.qrnote.repository.UserRepository;
import hack.jbnu.qrnote.token.JwtToken;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(User user) {
        user.passwordEncoding(passwordEncoder);
        userRepository.save(user);
    }

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    //초기 아이디, 비밀번호를 통한 로그인
    public LoginDTO login(String loginId, String password) {
        User findUser = userRepository.findByLoginId(loginId);

        if (findUser == null) {
            return new LoginDTO(false, "해당하는 아이디의 유저를 찾을 수 없습니다.", null);
        }
        if (passwordEncoder.matches(password, findUser.getPassword())) {
            String token = JwtToken.makeJwtToken(findUser.getLoginId());
            findUser.setToken(token);
            userRepository.save(findUser);
            return new LoginDTO(true, "로그인 성공", token);
        } else {
            return new LoginDTO(false, "비밀번호가 일치하지 않습니다.", null);
        }
    }

    //어플에 저장된 토큰을 사용한 자동 로그인
    public LoginDTO tokenLogin(String token) {

        if (token == null) {
            return new LoginDTO(false, "전달받은 토큰이 없습니다.", null);
        }
        Claims claims = JwtToken.parseJwtToken(token);
        User findUser = userRepository.findByLoginId((String) claims.get("loginId"));
        if (findUser == null) {
            return new LoginDTO(false, "유저를 찾을 수 없습니다.", null);
        }
        if (findUser.getToken().equals(token)) {
            return new LoginDTO(true, "로그인 성공", token);
        }
        return new LoginDTO(false, "토큰에 문제가 있어 로그인에 실패했습니다.", null);
    }
}
