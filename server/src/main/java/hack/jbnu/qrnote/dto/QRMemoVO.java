package hack.jbnu.qrnote.dto;

import hack.jbnu.qrnote.domain.Memo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class QRMemoVO {
    @NotNull
    Memo memo;
    @NotNull
    Long teamId;
    Double lat = 0.0;
    Double lng = 0.0;
}
