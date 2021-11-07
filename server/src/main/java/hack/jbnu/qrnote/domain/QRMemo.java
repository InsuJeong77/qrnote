package hack.jbnu.qrnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hack.jbnu.qrnote.dto.QRMemoVO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
public class QRMemo {
    @Id @GeneratedValue
    @Column(name = "qrmemo_id")
    private Long id;

    //접근 권한팀
    private Long authTeamId;
    //개인용일 때 qr메모의 주인을 찾기 위해
    private String writer;
    //좌표
    private Double lat;
    private Double lng;

    private String qrcode = String.valueOf(UUID.randomUUID());

    private LocalDateTime mTime = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "qrMemo", cascade = CascadeType.ALL)
    private List<Memo> memoList = new ArrayList<>();
    @OneToOne
    private Memo currMemo;

    public QRMemo() {
    }
    public QRMemo(Long teamId, Double lat, Double lng, String writer) {
        this.authTeamId = teamId;
        this.lat = lat;
        this.lng = lng;
        this.writer = writer;
    }

    public static QRMemo create(QRMemoVO qrMemoVO, String writer) {
        QRMemo qrMemo = new QRMemo(qrMemoVO.getTeamId(), qrMemoVO.getLat(), qrMemoVO.getLng(), writer);
        Memo memo = qrMemoVO.getMemo();
        memo.setWriter(writer);
        qrMemo.setCurrMemo(memo);
        qrMemo.memoList.add(memo);
        memo.setQrMemo(qrMemo);
        return qrMemo;
    }
}
