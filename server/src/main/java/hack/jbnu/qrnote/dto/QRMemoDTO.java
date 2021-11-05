package hack.jbnu.qrnote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QRMemoDTO {
    private boolean isSuccess;
    private String qrcode;
}
