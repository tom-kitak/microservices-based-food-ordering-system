# Project: Anni's Pizza

This project contains six microservices:
- authentication-microservice
- coupon-microservice
- menu-microservice
- order-microservice
- store-microservice
- user-microservice

The `authentication-microservice` is responsible for registering new users and authenticating current ones. After successful authentication, this microservice will provide a JWT token which can be used to bypass the security on the other microservices. This token contains the *memberId* of the user that authenticated.

The `coupon-microservice` is responsible for validating coupon codes and for choosing the best coupon code for minimizing order type. This microservice allows new coupon codes to be added and also to take a possible order and to check if a coupon code gives a better price and return the new price.

The `menu-microservice` is responsible for providing all available assortments of pizza that are available for ordering, while allowing configuring items with customizable toppings and also filtering out or warning users in case of allergens present.

The `order-microservice` is responsible for handling orders. It stores ongoing, placed, finished and canceled orders in a database.
The order microservice also collects order details. First it collects the order time and location. If the location is not provided the Order will query the Users for the preferred location.

The `store-microservice` is responsible for keeping track of possible store locations. This microservice allows the regional manager to add new or remove existing stores. The store microservice is also responsible for encapsulating dummy email functionality.

The `user-microservice` is responsible for keeping track of allergies that the user has and the location of their preferred store. The user is authenticated using the token given from the authentication microservice. The allergens are queried by the menu and order microservices in order to filter out items or notify the user that they might be allergic to the item.

The `domain` and `application` packages contain the code for the domain layer and application layer. The code for the framework layer is the root package as *Spring* has some limitations on were certain files are located in terms of autowiring.

## Running the microservices

You can run the microservices individually by starting the Spring applications. Then, you can use *Postman* to perform the different requests:

### Step 1: Authentication
#### Register:
![image](instructions/register.png)

#### Authenticate:
In case you are a regional manager, change the role first:

![image](instructions/change_role.png)

![image](instructions/authenticate.png)

In case you are a store manager, register with the name `StoremanagerX`, where instead of X you put the id of the store you're an admin of. Then change the role to `store_manager`. Then authenticate.

### Step 2: User Registration (If you are a customer)
As a customer, you can set your allergens in your details so that you don't have to manually add them every time you order. 

#### To do this, first you add the allergens: 

![image](instructions/user_add_allergen.png)

#### You can also remove any allergen:

![image](instructions/user_remove_allergen.png)

#### You are able to see all of your selected allergens:

![image](instructions/user_get_allergen.png)

### Step 3: Adding stores to database (If you are a regional manager)
By default, we store 10 stores on the database

To add/delete stores you have to be a regional manager.

#### Adding stores:

![image](instructions/add_store.png)

#### Deleting stores:

![image](instructions/remove_store.png)

#### Viewing all stores:

![image](instructions/get_all_stores.png)

#### Sending emails:

![image](instructions/send_emails.png)

#### Viewing emails of a specific store:

![image](instructions/email_by_store.png)

#### Viewing emails of all stores:

![image](instructions/get_all_emails.png)

### Step 4: Adding coupons to database (If you are a regional manager)
To add/delete coupons you have to be a regional manager. 
You can add coupons either in the form of a "discount", or a "one off" (get the most expensive item from order for free)

#### Adding coupons:

![image](instructions/add_coupon.png)

#### Deleting coupons:

![image](instructions/remove_coupon.png)

#### Viewing all coupons:

![image](instructions/get_coupons.png)

### Step 5: Adding toppings and pizza's to the menu (If you are a regional manager)
To add/delete toppings and pizzas from the menu you have to be a regional manager.

#### Adding allergen:

![image](instructions/add_allergen.png)

#### Adding toppings (possibly containing allergens):

![image](instructions/add_topping.png)

#### Removing toppings:

![image](instructions/remove_topping.png)

#### Viewing all toppings:

![image](instructions/get_toppings.png)

#### Adding pizzas:

![image](instructions/add_pizza.png)

#### Removing pizzas:

![image](instructions/remove_pizza.png)

#### Viewing all pizzas:

![image](instructions/get_pizzas.png)

#### Filter all toppings by user's allergens:

![image](instructions/filter_topping.png)

#### Filter all pizzas by user's allergens:

![image](instructions/filter_pizza.png)

### Step 6: Ordering

As a customer, this is the process of ordering.

#### Starting an order:

![image](instructions/start_order.png)

#### Setting the location of an order (it has to be the location of a valid store):

![image](instructions/set_location.png)

#### Setting the desired time of an order:

![image](instructions/set_time.png)

#### Adding a pizza to an order:

![image](instructions/order_add_pizza.png)

#### Removing a pizza from an order:

![image](instructions/order_remove_pizza.png)

#### Editing a pizza (By adding/removing toppings from it):

Adding topping:

![image](instructions/order_add_topping.png)

Removing topping:

![image](instructions/order_remove_topping.png)

#### Placing an order:

![image](instructions/place_order.png)

#### Cancelling an order:

![image](instructions/cancel_order.png)

#### Applying a coupon to an order (it has to be a valid coupon):

![image](instructions/order_add_coupon.png)