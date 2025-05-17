package com.licenta.licenta.engine.workflow.dto;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplyDTO {
    private String correlationId;
    private String textMessage;
    private FormRecordDTO replyForm;
}
