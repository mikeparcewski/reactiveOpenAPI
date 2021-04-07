package com.accenture.cloudnative.reference.reactoropenapi.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * The data model for entity.  We keep this separate so have the ability
 * to make underlying changes to data model without impacting the contract
 * or vice/versa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Vendor implements DomainObject {

  @Id
  private String id;

  @CreatedDate
  private LocalDateTime createDatetime = LocalDateTime.now();

  @CreatedBy
  private String createUserId;

  @LastModifiedDate
  private LocalDateTime lastUpdateDatetime = LocalDateTime.now();

  @LastModifiedBy
  private String lastUpdateUserId;

  private String name;
  private String description;
  private List<ContactDetail> contactDetails;

}
