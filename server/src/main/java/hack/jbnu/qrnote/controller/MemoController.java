package hack.jbnu.qrnote.controller;

import hack.jbnu.qrnote.api.ApiMessage;
import hack.jbnu.qrnote.domain.Memo;
import hack.jbnu.qrnote.domain.QRMemo;
import hack.jbnu.qrnote.dto.QRMemoDTO;
import hack.jbnu.qrnote.validator.MemoValidator;
import hack.jbnu.qrnote.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/memo")
public class MemoController {
    private final MemoService memoService;
    private final SmartValidator smartValidator;
    private final MemoValidator memoValidator;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody Memo memo, BindingResult result) {
        smartValidator.validate(memo, result);
        if (result.hasErrors()) {
            //에러메시지
            return ResponseEntity.ok(ApiMessage.builder().data(new QRMemoDTO(false, null)).message("에러 메시지").build());
        }
        QRMemo qrMemo = memoService.create(memo);
        //성공메세지
        return ResponseEntity.ok(ApiMessage.builder().data(new QRMemoDTO(true, qrMemo.getQrcode())).message("성공 메시지").build());
    }

    @PostMapping("/modify")
    public ResponseEntity modify(@RequestBody Memo memo, BindingResult result) {
        memoValidator.validate(memo, result);
        if (result.hasErrors()) {
            return ResponseEntity.ok(ApiMessage.builder().data(false).message("에러 메시지").build());
        }
        memoService.modify(memo);
        return ResponseEntity.ok(ApiMessage.builder().data(true).message("성공 메시지").build());
    }
}
