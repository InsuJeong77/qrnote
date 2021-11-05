package hack.jbnu.qrnote.validator;

import hack.jbnu.qrnote.domain.Memo;
import hack.jbnu.qrnote.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemoValidator implements Validator {
    private final MemoService memoService;
    @Override
    public boolean supports(Class<?> clazz) {
        return Memo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Memo memo = (Memo) target;
        Memo DBMemo = memoService.findById(memo.getId());
        if (memo.getTitle().equals(DBMemo.getTitle()) && memo.getContents().equals(DBMemo.getContents())) {
            errors.rejectValue("contents", null, "메모가 수정되지 않았습니다.");
        }
    }
}
