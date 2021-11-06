package hack.jbnu.qrnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Memo {
    @Id
    @GeneratedValue
    @Column(name = "memo_id")
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    private LocalDateTime gTime = LocalDateTime.now();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "qrmemo_id")
    private QRMemo qrMemo;
}
