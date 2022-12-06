# Requirements
## 1. Functional Requirements

### 1.1 Must Haves
* Users must be able to create an account with a username and a password -> Generate a unique ID
* Users need to have an ID (member ID) associated with their account.
* Users need to authenticate themselves with an ID associated with their account to determine who they are and what they can do on the platform.
* The store must have a list of pizzas available to order from
* The customer must be able to make an order
* The customer must be able to remove or edit pizzas currently on their order
* The customer must be able to specify at what time they want their pizza ready
* The customer must be able to specify at which location they want to pick up the pizza

### 1.2 Should Haves
* The pizzas available to order from should have a default and custom set of configurable toppings per type of pizza
* A customer should be able to mention their allergies
* A customer should be able to filter out pizzas based on their allergens
* A customer should be notified when his pizzas are ready or in case of a cancellation
* An admin should be able to upload coupon codes in a predefined format (([a-z]|[A-Z]){4}[0-9]{2})
* The store should be notified when an order is created
* The customer can use a "buy one, get one free" coupon code
* The customer can use an "x% discount" coupon code
* If the customer submits multiple coupons the cheapest should be applied
* The coupons can only be used once
* The regional manager could see all currently places orders and cancel any selection of orders if needed

### 1.3 Could Haves
* A customer could be warned when they select a pizza that contains allergens for them
* An admin could be able to configure coupon codes to expire in a limited amount of time
* A customer could be able to edit the order only if there are more than 30 minutes before the order should be ready

### 1.4 Would/Won't Haves
* The customer can use special coupon codes that will only work with a specific combination of products

## 2. Non-functional Requirements
* The system should be written in Java programming language (version 11)
* The system will not have a GUI (Graphical User Interface)
* The system should use microservices
* A first fully working version of the system  shall be delivered until 23 December
* The system should be built with Spring Boot (Spring framework) and Gradle
* The member ID together with the password serves as the credentials of the account
* The password needs to be stored safely
* Security should be done using Spring Security