package com.digital.telco.inventory.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AuditableEntity {

    @CreatedDate
    @Column(name="created_at")
    protected LocalDateTime createdAt;


    @CreatedBy
    @Column(name = "created_by")
    protected Long createdBy;

    @LastModifiedDate
    @Column(name = "modified_at")
    protected LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column(name="modified_by")
    protected Long modifiedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = this.modifiedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

}