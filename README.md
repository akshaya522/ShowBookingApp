# Show Booking Application

2 User Roles: Admin / Buyer 

# How to run

1. Go to app: `cd ./demo`
3. Run `mvn spring-boot:run`
4. Spring Shell will open 
5. Enter commands and parameters 

# Commands 

For Admin User 
1. To select User Role: `User` Admin/Buyer 
2. To Setup Show: `Setup` --rows  --seats --cancellationMins

For Buyer User 
1. To check show seat Availability: `Availability` 
2. To Book Ticket: `Book` --showId --seats --phoneNo
3. To Cancel Ticket: `Cancel` --ticketNo --phoneNo
