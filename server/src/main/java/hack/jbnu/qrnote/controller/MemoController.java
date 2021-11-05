package hack.jbnu.qrnote.controller;

import hack.jbnu.qrnote.api.ApiMessage;
import hack.jbnu.qrnote.domain.Memo;
import hack.jbnu.qrnote.domain.QRMemo;
import hack.jbnu.qrnote.domain.Team;
import hack.jbnu.qrnote.domain.User;
import hack.jbnu.qrnote.dto.QRMemoDTO;
import hack.jbnu.qrnote.dto.QRMemoVO;
import hack.jbnu.qrnote.repository.TeamRepository;
import hack.jbnu.qrnote.service.UserService;
import hack.jbnu.qrnote.token.JwtToken;
import hack.jbnu.qrnote.validator.MemoValidator;
import hack.jbnu.qrnote.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {
    private final MemoService memoService;
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final SmartValidator smartValidator;
    private final MemoValidator memoValidator;

    //진입점
//    @GetMapping("/list")
//    public String list(@RequestHeader("token") String token, Model model){
//        String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
//        User user = userService.findByLoginId(loginId);
//        List<Long> teamIds = user.getUserTeamList().stream().map(t -> t.getId()).collect(Collectors.toList());
//        //부서 폴더 가져오기, Teamid로 매핑
//        Map<Long, List<QRMemo>> teamMemo = memoService.findQRMemoMapByTeamIds(teamIds);
//        List<QRMemo> personalMemo = memoService.findPersonalMemo(loginId);
//        List<Team> teams = teamRepository.findByTeamIds(teamIds);
//        teamMemo.put(0L, personalMemo);
//
//        model.addAttribute("teamMemo", teamMemo);
//        model.addAttribute("teams", teams);
//        model.addAttribute("loginId", loginId);
//        return "memo/list";
//    }

    @GetMapping("/sidebar")
    public ResponseEntity getTeamList(@RequestHeader("token") String token) {
        String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
        User user = userService.findByLoginId(loginId);
        List<Long> teamIds = user.getUserTeamList().stream().map(t -> t.getId()).collect(Collectors.toList());
        List<Team> teams = teamRepository.findByTeamIds(teamIds);
        return ResponseEntity.ok(ApiMessage.builder().data(teams).message("디렉토리 목록을 가져왔습니다.").build());
    }

    @GetMapping("/list/{teamId}")
    public ResponseEntity getMemoListByTeamId(@RequestHeader("token") String token, @PathVariable("teamId") Long teamId) {
        List<QRMemo> memos;
        //개인용 메모일 때
        if (teamId == 0) {
            String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
            memos = memoService.findPersonalMemo(loginId);
        } else {
            memos = memoService.findByTeamId(teamId);
        }
        return ResponseEntity.ok(ApiMessage.builder().data(memos).message("메모들을 가져왔습니다."));
    }

//    @PostMapping("/create")
//    public ResponseEntity create(@RequestBody QRMemoVO qrMemoVO,
//                                 @RequestHeader("token") String token, BindingResult result) {
//        smartValidator.validate(qrMemoVO.getMemo(), result);
//        smartValidator.validate(qrMemoVO, result);
//        if (result.hasErrors()) {
//            //에러메시지
//            return ResponseEntity.ok(ApiMessage.builder().data(new QRMemoDTO(false, null)).message("에러 메시지").build());
//        }
//        String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
//        QRMemo qrMemo = memoService.create(qrMemoVO, loginId);
//        //성공메세지
//        return ResponseEntity.ok(ApiMessage.builder().data(new QRMemoDTO(true, qrMemo.getQrcode())).message("성공 메시지").build());
//    }

    //memo객체에 QRMemo가 필수로 있어야함
    @PostMapping("/modify")
    public ResponseEntity modify(@RequestBody Memo memo, @RequestHeader("token") String token, BindingResult result) {
        memoValidator.validate(memo, result);
        if (result.hasErrors()) {
            return ResponseEntity.ok(ApiMessage.builder().data(false).message("에러 메시지").build());
        }
        String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
        memoService.modify(memo, loginId);
        return ResponseEntity.ok(ApiMessage.builder().data(true).message("성공 메시지").build());
    }

    @GetMapping("/read/qr")
    public ResponseEntity readByQRcode(@RequestParam String qrcode, @RequestHeader("token") String token) {
        String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
        return memoService.readByQRcode(qrcode, loginId);
    }

    @GetMapping("/read/id")
    public ResponseEntity readByQRMemoId(@RequestParam Long QRMemoId, @RequestHeader("token") String token) {
        String loginId = (String) JwtToken.parseJwtToken(token).get("loginId");
        return memoService.readByMemoId(QRMemoId, loginId);
    }
}
