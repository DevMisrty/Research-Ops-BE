package com.OneToOne.RelationalMapping.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Attachment {

    @Id
    Integer attachmentId;
    String url;

}
