package org.prgrms.nabimarketbe.global;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @PrePersist
    public void onPrePersist() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime parsedCreateDate = LocalDateTime.parse(
                customLocalDateTimeFormat,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        this.createdAt = parsedCreateDate;
        this.modifiedAt = parsedCreateDate;
    }

    @PreUpdate
    public void onPreUpdate() {
        String customLocalDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime parsedUpdateDate = LocalDateTime.parse(
                customLocalDateTimeFormat,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        this.modifiedAt = parsedUpdateDate;
    }
}
