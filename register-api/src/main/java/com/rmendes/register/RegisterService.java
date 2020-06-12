package com.rmendes.register;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.LoggerFactory;

import com.rmendes.register.exceptions.NegativeBalanceOnCreationException;
import com.rmendes.register.model.Account;
import com.rmendes.register.model.Result;

@Path("/register")
public class RegisterService {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Account findById(@PathParam("id") Long id) {
		try {
			return Account.findById(id);
		}catch (Exception e) {
			LOGGER.error("Error on find by id", e.getCause());
			throw e;
		}
	}

	@GET
	@Path("/blocked")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Account> findBlocked(@PathParam("id") Long id) {
		try {
			return Account.list("blocked", true);
		}catch (Exception e) {
			LOGGER.error("Error on find by id", e.getCause());
			throw e;
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Account> findAll() {
		try {
			return Account.listAll();
		}catch (Exception e) {
			LOGGER.error("Error on find by id", e.getCause());
			throw e;
		}
	}




	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Result create(@Valid Account account) {
		try {
			if(account.balance.compareTo(BigDecimal.ZERO) > 0) {
				account.persist();
				return new Result("Account registered!"); 
			}else {
				throw new NegativeBalanceOnCreationException();
			}
		}catch (Exception e) {
			LOGGER.error("Error on create: "+e.getCause());
			throw e;
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Account remove(@Valid Account account) {
		try {
			account.delete();
			return account;
		}catch (Exception e) {
			LOGGER.error("Error on create: "+e.getCause());
			throw e;
		}
	}


	@PUT
	@Path("/updateBalance/{id}/{balance}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Account update(@PathParam("id") Long id, @PathParam("balance") BigDecimal balance) {
		try {
			Account account = Account.findById(id);
			account.balance = balance;
			return account;
		}catch (Exception e) {
			throw new WebApplicationException("Error on update balance: "+e.getMessage(), Status.INTERNAL_SERVER_ERROR);
		}
	}

}