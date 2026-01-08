package com.OneToOne.RelationalMapping.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Message {

    @Id
    Integer messageId;
    String receiver;
    String content;
    String sender;

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL )
    @JoinColumn(name = "attachment_id",referencedColumnName = "attachmentId", nullable = false)
    Attachment attachment;
}
