package hack.jbnu.qrnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    //좌표


    private String qrcode = String.valueOf(UUID.randomUUID());

    @JsonIgnore
    @OneToMany(mappedBy = "qrMemo", cascade = CascadeType.ALL)
    private List<Memo> memoList = new ArrayList<>();

    public static QRMemo create(Memo memo) {
        QRMemo qrMemo = new QRMemo();
        qrMemo.memoList.add(memo);
        memo.setQrMemo(qrMemo);
        return qrMemo;
    }
}
