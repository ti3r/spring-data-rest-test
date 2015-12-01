package com.perficient.test;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;

public class RestTest extends SpringDataRestTestApplicationTests {

	@Autowired
	WebApplicationContext ctx;
	
	MockMvc mockMvc;
	
	@Before
	public void init(){
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Test
	public void testCreate() throws Exception{
		createOneItemAndAssert();
	}

	private void createOneItemAndAssert() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.post("/items").contentType(MediaType.APPLICATION_JSON)
				.content(getJsonForNewObject()))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			;
	}

	@Test
	public void testGetItems() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/items").contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.page").exists())
		;
	}
	
	@Test
	public void testCreateItemAndGet() throws Exception{
		createOneItemAndAssert();
		mockMvc.perform(MockMvcRequestBuilders.get("/items/{0}", 1).contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$.price").exists())
		.andExpect(MockMvcResultMatchers.jsonPath("$._links").exists())
		;
	}
	
	private byte[] getJsonForNewObject() {
		Item i = new Item("Test description", BigDecimal.ONE);
		Gson gson = new Gson();
		String json =  gson.toJson(i);
		return json.getBytes();
	}
	
}
