package com.rmendes.register.model;

import java.math.BigDecimal;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Account extends PanacheEntity {
	
	public Long number;
	
	public BigDecimal balance;
	
	public boolean blocked;
	
	

}
