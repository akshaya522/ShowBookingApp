package com.example.ShowBookingApplication;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShowBookingApplication implements CommandLineRunner {

	@Autowired
	BookTicketService bookTicketService;

	public static void main(String[] args) {

		if(args[0].equals("--firstName=Sergey")){
			SpringApplication.run(ShowBookingApplication.class, args);
		}

		if(args[0].equals("View")){
			// testfun("View");
		}
	}

	@Override 
	public void run(String... args) throws IOException {
		System.out.println("vghsdfbsdnhfbksdjfnsd");
		if(args[0].equals("--firstName=Sergey")){
			System.out.println("im inside here");
			String test = this.bookTicketService.test();
			System.out.println(test);
		}
	}

}
