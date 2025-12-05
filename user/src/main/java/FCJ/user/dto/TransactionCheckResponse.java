package FCJ.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Transaction Check Response DTO")
public class TransactionCheckResponse {
    @Schema(description = "MoMo transaction ID", example = "txn_abc123xyz")
    private String momoTransId;

    @Schema(description = "Whether this transaction has been processed", example = "true")
    private boolean processed;

    @Schema(description = "Current membership level if transaction was processed", example = "VIP")
    private String membership;

    @Schema(description = "Message describing the transaction status", example = "Transaction already processed")
    private String message;
}

