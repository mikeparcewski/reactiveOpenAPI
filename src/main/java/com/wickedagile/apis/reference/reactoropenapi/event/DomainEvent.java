package com.wickedagile.apis.reference.reactoropenapi.event;

import com.wickedagile.apis.reference.reactoropenapi.domain.DomainObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DomainEvent {

  DomainOperation operation;
  DomainObject object;

}
