package hack.jbnu.qrnote.service;

import hack.jbnu.qrnote.api.ApiMessage;
import hack.jbnu.qrnote.domain.Memo;
import hack.jbnu.qrnote.domain.QRMemo;
import hack.jbnu.qrnote.domain.Team;
import hack.jbnu.qrnote.domain.User;
import hack.jbnu.qrnote.dto.QRMemoVO;
import hack.jbnu.qrnote.repository.MemoRepository;
import hack.jbnu.qrnote.repository.QRMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
    private final QRMemoRepository qrMemoRepository;
    private final UserService userService;

    public QRMemo create(QRMemoVO qrMemoVO, String loginId) {
        QRMemo qrMemo = QRMemo.create(qrMemoVO, loginId);
        qrMemoRepository.save(qrMemo);
        return qrMemo;
    }

    public Memo findById(Long id) {
        return memoRepository.findById(id).get();
    }

    public void modify(Memo memo, String loginId) {
        QRMemo qrMemo = findById(memo.getId()).getQrMemo();
        memo.setQrMemo(qrMemo);
        memo.setGTime(LocalDateTime.now());
        memo.setId(null);
        memoRepository.save(memo);
        qrMemo.setMTime(memo.getGTime());
        qrMemo.setCurrMemo(memo);
        qrMemoRepository.save(qrMemo);
    }

    public ResponseEntity<ApiMessage> readByQRcode(String qrcode, String loginId) {
        QRMemo byQrcode = qrMemoRepository.findByQrcode(qrcode);
        if (byQrcode == null) {
            return ResponseEntity.ok(ApiMessage.builder().data(null).message("잘못 된 qr코드 입니다.").build());
        } else if (!userService.findByLoginId(loginId).getUserTeamList().stream().map(ut ->
                ut.getTeam().getId()).collect(Collectors.toList()).contains(byQrcode.getAuthTeamId())) {
            return ResponseEntity.ok(ApiMessage.builder().data(null).message("접근 권한이 없습니다.").build());
        }
        //메모만 가져옴, 버전관리를 위한 메모장 리스트는 나중에 다시 구현
        Memo currMemo = byQrcode.getCurrMemo();
        return ResponseEntity.ok(ApiMessage.builder().data(currMemo).message("qr코드 인식에 성공하였습니다.").build());
    }

    public ResponseEntity readByMemoId(Long qrMemoId, String loginId) {
        QRMemo qrMemo = qrMemoRepository.findById(qrMemoId).get();
        if (!userService.findByLoginId(loginId).getUserTeamList().contains(qrMemo)) {
            return ResponseEntity.ok(ApiMessage.builder().data(null).message("접근 권한이 없습니다.").build());
        }
        //메모만 가져옴, 버전관리를 위한 메모장 리스트는 나중에 다시 구현
        Memo currMemo = qrMemo.getCurrMemo();
        return ResponseEntity.ok(ApiMessage.builder().data(currMemo).message("메모를 읽어들였습니다.").build());
    }

    public Map<Long, List<QRMemo>> findQRMemoMapByTeamIds(List<Long> teamIds) {
        List<QRMemo> qrMemos = qrMemoRepository.findQRMemosByTeamIds(teamIds);
        //teamid를 key
        Map<Long, List<QRMemo>> mapResult = qrMemos.stream()
                .collect(Collectors.groupingBy(QRMemo::getAuthTeamId));
        return mapResult;
    }

    public List<QRMemo> findPersonalMemo(String loginId) {
        return qrMemoRepository.findPersonalMemo(loginId);
    }

    public List<QRMemo> findByTeamId(Long teamId) {
        return qrMemoRepository.findByTeamId(teamId);
    }

    public List<Memo> findVCSBymemoId(Long qrmemoId) {
        return memoRepository.findVCSByQRMemoId(qrmemoId);
    }
}
