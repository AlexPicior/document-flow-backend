package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.helper.SpringContextHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class SendEmailTask extends AbstractWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.SEND_EMAIL_TASK;

    public static final String TO = "to";
    public static final String CC = "cc";
    public static final String SUBJECT = "subject";
    public static final String MESSAGE = "message";
    public static final String ATTACHMENTS = "attachments";

    private List<String> to;
    private List<String> cc;
    private String subject;
    private String message;
    private List<String> attachments;

    public SendEmailTask(Map<String, TaskProperty> properties) {
        this.to = (List<String>) properties.get(TO).getValue();
        this.cc = (List<String>) properties.get(CC).getValue();
        this.subject = (String) properties.get(SUBJECT).getValue();
        this.message = (String) properties.get(MESSAGE).getValue();
        this.attachments = (List<String>) properties.get(ATTACHMENTS).getValue();
    }

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        try {
            JavaMailSender javaMailSender = SpringContextHelper.getBean(JavaMailSender.class);
            MimeMessage email = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(email, true);
            helper.setFrom("no.reply.licenta.alex@gmail.com");
            helper.setTo(to.toArray(new String[0]));
            helper.setCc(cc.toArray(new String[0]));
            helper.setSubject(subject);
            helper.setText(message);

            List<FormRecordDTO> contents = attachments.stream()
                    .map(attachment -> (FormRecordDTO)inputParameters.get(attachment)).toList();
            //TODO resolve sending of attachments
//            FileSystemResource file = new FileSystemResource(new File(attachmentPath));
//            helper.addAttachment(file.getFilename(), file);

            javaMailSender.send(email);
        } catch (MessagingException e) {
        }

//        System.out.println("Properties of " + TYPE + ": " + TO + ": " + String.join(",", to) + ", "
//                + CC + ": " + String.join(",", cc) + ", "
//                + MESSAGE + ": " + message
//                + SUBJECT + ": " + subject
//                + ATTACHMENTS + ": " + String.join(",", attachments));
        return inputParameters;
    }
}
