package com.wickedagile.apis.reference.reactoropenapi.domain;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * The data model for contactdetails.  We keep this separate so have the ability
 * to make underlying changes to data model with impacting the contract
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactDetail {

  public enum DetailTypeEnum {
    TYPE_OFFICE,
    TYPE_REPRESENTATIVE,
    TYPE_ALTERNATIVE
  };

  @CreatedDate
  private LocalDateTime createDatetime = LocalDateTime.now();

  @CreatedBy
  private String createUserId;

  @LastModifiedDate
  private LocalDateTime lastUpdateDatetime = LocalDateTime.now();

  @LastModifiedBy
  private String lastUpdateUserId;

  private DetailTypeEnum detailType;
  private String name;
  private String email;
  private String phone;

}
