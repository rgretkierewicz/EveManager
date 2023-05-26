Title: Consultation Scheduling Application 

This application is used to schedule consultation appointments for customers. The application has the functionality to add, update, and delete both appointment and customer information. Additionally, reports are accessible for information about these customers and appointments. 

Rosemary Gretkierewicz - rgretk1@wgu.edu, Application Version 2, 1/24/2023

IntelliJ IDEA Community Edition 2021.1.3, Java SE 17.0.1, JavaFX-SDK-17.0.1

Running the program: Launch the application from 'Main', login with the username 'test' and the password 'test'.
From there all the appointments are displayed in a TableView. You can add, update, or delete appointments.
The 'Customer Info' button will take you to a TableView displaying all customers. You can add, update, or delete customers from here. 
The 'Reports' button will take you to a screen that will allow you to select from: 'Cancellation Reports', 'Contact Schedule Reports', or 'Appointments Reports'.
The application can be terminated by navigating to the 'Appointments Info' screen and hitting the 'Exit' button in the bottom right.

Additional Report: The application will record each time an appointment is deleted before its scheduled start time. Data for that appointment will be added to a text file and an AppointmentCancellation scene will allow users to view a report display of all canceled appointments. 

mysql-connector-java-8.0.26