package hack.jbnu.qrnote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemoDTO {
    private Long id;
    private String title;
    private String contents;
    private String gTime;
    private String writer;
}
