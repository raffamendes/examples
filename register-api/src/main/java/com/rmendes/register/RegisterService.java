package com.rmendes.register;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.LoggerFactory;

import com.rmendes.register.exceptions.NegativeBalanceOnCreationException;
import com.rmendes.register.model.Account;

@Path("/register")
public class RegisterService {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);
	
	@Inject
	Validator validator;

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("client")
	public Response findById(@PathParam("id") Long id) {
		Account a = Account.findById(id);
		if(a == null) {
			return Response.status(Status.NOT_FOUND).build();
		}else {
			return Response.ok(a).build();
		}
	}

	@GET
	@Path("/blocked")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("client")
	public Response findBlocked(@PathParam("id") Long id) {
		List<Account> accounts = Account.list("blocked", true);
		if(accounts.size() == 0) {
			return Response.status(Status.NOT_FOUND).build();
		}else {
			return Response.ok(accounts).build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("client")
	public Response findAll() {
		try {
			return Response.ok(Account.listAll()).build();
		}catch (Exception e) {
			LOGGER.error("Error on find by id", e.getCause());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}




	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed("client")
	public Response create(@Valid Account account) {
		try {
			if(account.balance.compareTo(BigDecimal.ZERO) > 0) {
				account.persist();
				return Response.ok(account).build();
			}else {
				return Response.status(Status.BAD_REQUEST).entity(new NegativeBalanceOnCreationException()).build();
			}
		}catch (Exception e) {
			LOGGER.error("Error on create: "+e.getCause());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed("client")
	public Response remove(@Valid Account account) {
		try {
			account.delete();
			return Response.ok(account).build();
		}catch (Exception e) {
			LOGGER.error(e.getLocalizedMessage());
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}


	@PUT
	@Path("/updateBalance/{id}/{balance}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed("client")
	public Response update(@PathParam("id") Long id, @PathParam("balance") BigDecimal balance) {
		try {
			Account account = Account.findById(id);
			account.balance = balance;
			return Response.ok(account).build();
		}catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e).build();
		}
	}

}