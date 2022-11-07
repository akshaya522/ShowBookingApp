# Show Booking Application

Java application for Show setup and bookings. 
---- 
Admins can setup and check show details. Buyers can check seat availability, book tickets and cancel tickets. 
Implemented using SpringBoot and Spring Shell. 

# How to run

1. Go to app: `cd ./demo`
3. Run `mvn spring-boot:run`
4. Spring Shell will open for command-line inputs 
5. Enter `help` to view commands and arguments
6. Enter commands


# Commands 

For Admin User 
1. To select User Role: `User` `<Admin/Buyer>` Admin/Buyer (Eg: User Admin)
2. To Setup Show: `Setup` --rows `<Number of rows>` --seats `<Number of seats per row>` --cancellationMins `<Cancellation window minutes>` (Eg: Setup --rows 7 --seats 10 --cancellationMins 10)

For Buyer User 
1. To check show seat Availability: `Availability` `<Show Id>` (Eg: Availability 1)
2. To Book Ticket: `Book` --showId `<ShowId>` --seats `<Seat List>` --phoneNo `<Phone #>` (Eg: Book --showId 1 --seats "A1,A2" --phoneNo 81234567)
3. To Cancel Ticket: `Cancel` --ticketNo `<Ticket #>` --phoneNo `<Phone #>` (Eg: Cancel --ticketNo 1 --phoneNo 81234567)
