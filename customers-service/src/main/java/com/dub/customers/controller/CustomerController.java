package com.dub.customers.controller;

import java.net.URI;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.dub.customers.exceptions.CustomerNotFoundException;
import com.dub.customers.exceptions.DuplicateCustomerException;
import com.dub.customers.model.Customer;
import com.dub.customers.model.CustomerList;
import com.dub.customers.services.CustomerService;

//@RequestMapping(value = "/api")
@RestController
public class CustomerController {
	 
	@Value("${resourceUriHost}")
	private String resourceUriHost;
	
	@Value("${resourceUriPort}")
	private String resourceUriPort;
	
	@Value("${resourceUriPath}")
	private String resourceUriPath;
	
	@Value("${ENABLE_SLOW}")
	private boolean enableSlow;
	
	

	@Autowired
	private CustomerService customerService;

	@RequestMapping(value = "/customerList", 
						method = RequestMethod.GET)
	public CustomerList customerList() {

		CustomerList customerList = customerService.getCustomerList();
			
	
		System.out.println("enableSlow " + enableSlow);
		if (enableSlow) {
			randomlyRunLong();
		}
		
		return customerList;
	}

	@RequestMapping(value = "/createCustomer", 
			method = RequestMethod.POST)
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {

		HttpHeaders headers = new HttpHeaders();
			
		Customer newCustomer = customerService.createCustomer(customer);
				
		UriComponents ucb = UriComponentsBuilder.newInstance()
			            .scheme("http")
			            .host(resourceUriHost)
			            .port(resourceUriPort)
			            .path(resourceUriPath + "/{id}")
			            .buildAndExpand(newCustomer.getId());
			            
		URI uri = ucb.toUri();
				
		headers.setLocation(uri);
		
		return new ResponseEntity<>(newCustomer, headers, HttpStatus.OK);
	}


	@RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
	public ResponseEntity<Customer> getCustomer(@PathVariable("id") long id) {
			
		Customer customer = customerService.getCustomerById(id);
		
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/customer/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Customer> updateCustomer(
										@PathVariable("id") long id, 
										@RequestBody Customer customer) {
					
		// check that customer exists
		customerService.getCustomerById(id);
		customerService.updateCustomer(customer);
		return new ResponseEntity<>(customer, HttpStatus.OK);			
	}
	
	@RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") long id) {
		
		// check that customer exists
		customerService.getCustomerById(id);
		customerService.deleteCustomer(id);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	
	// for Zuul fallback demonstration only
	private void sleep() {
        try {
        	System.out.println("Before sleep");
            Thread.sleep(11000);
        	System.out.println("After sleep");
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("exception: " + e);
        }
    }
	
	private void randomlyRunLong() {
		Random rand = new Random();

		int randomNum = rand.nextInt((3 - 1) + 1) + 1;

		if (randomNum == 3) {
			System.out.println("Going to sleep...");
			sleep(); 
		}
	}
	
}
