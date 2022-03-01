package com.wickedagile.apis.reference.reactoropenapi.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

// annotation allows for serialization of generic types
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
public interface DomainObject {
}
