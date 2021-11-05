package hack.jbnu.qrnote.service;

import hack.jbnu.qrnote.domain.Memo;
import hack.jbnu.qrnote.domain.QRMemo;
import hack.jbnu.qrnote.repository.MemoRepository;
import hack.jbnu.qrnote.repository.QRMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final QRMemoRepository qrMemoRepository;

    public QRMemo create(Memo memo) {
        QRMemo qrMemo = QRMemo.create(memo);
        qrMemoRepository.save(qrMemo);
        return qrMemo;
    }

    public Memo findById(Long id) {
        return memoRepository.findById(id).get();
    }

    public void modify(Memo memo) {
        memo.setId(null);
        memoRepository.save(memo);
    }
}
