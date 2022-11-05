package com.example.ShowBookingApplication;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.Bootstrap;

@SpringBootApplication
public class ShowBookingApplication implements CommandLineRunner {

	@Autowired
	BookTicketService bookTicketService;

	public static void main(String[] args) throws IOException {

		SpringApplication.run(ShowBookingApplication.class, args);
			Bootstrap.main(args);

		if(args[0].equals("View")){
			// testfun("View");
		}
	}

	@Override 
	public void run(String... args) throws IOException {
	}

}
