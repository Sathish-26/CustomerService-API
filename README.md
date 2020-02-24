# CustomerService-API
This API is used mainly for customer related services like adding a new customer, retrieving customers list, 
retrieving customer based on Id/First/Last Name and Updating the customer address.

Runtime Requirements:
JDK version - 11 and above.

How to Build the Project:
Open the project root folder and navigate to pom.xml file location.
Then open command prompt and execute 'mvn clean install' command.
Once the build is successful, the project can be imported in any IDE's.

Functionalities supported:
1. Add a new Customer - To add a new customer into the system.
2. Retrieve All Customers - To retrieve all available customers from the system.
3. Retrieve Customer based on Id - To retrieve customer based on the Id.
4. Retrieve Customer based on First/Last Name - To retrieve customer based on the First Name/Last Name.
5. Update the address of the customer - To update the address of the customer.

Swagger Endpoint:
http://localhost:8082/customerApp/swagger-ui.html