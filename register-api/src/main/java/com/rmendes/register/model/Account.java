package com.rmendes.register.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Account extends PanacheEntity {
	
	@NotNull(message = "Account may not be blank")
	public Long number;
	
	public BigDecimal balance;
	
	public boolean blocked;
	
	

}
