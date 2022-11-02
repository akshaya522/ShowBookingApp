package com.example.ShowBookingApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShowController {

    @Autowired
    private ShowRepository showRepository;

    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @PostMapping("/saveShow")
    public String saveShow(@RequestBody Show show){
        showRepository.save(show);
        return "Show saved...";
    }

    @GetMapping("/getAllShows")
    public List<Show> getAll(){
        return showRepository.findAll();
    }

    @GetMapping("/getSeats")
    public List<String> getSeats(){
        Show show = showRepository.findById(1L).get();
        return show.getAllSeats(show.getNumOfRows(), show.getNumOfSeatsPerRow());
    }
    
}