# Marketplace

A virtual marketplace for Customers and Sellers to buy and list products. 


## Usage

To compile and run the project, run the main method of the **Core** class.

## Submission

[Student Name] - Submitted Report on Brightspace.
[Student Name] - Submitted Vocareum Workspace.

# Classes

## AccountException
An AccountException is thrown when the user is not the appropriate role for the current mode that the Marketplace session is in (Customer or Seller).

## AccountManager

This class is responsible for the creation of accounts that fall into either a customer or seller role. It contains methods for signing up a new user as well as logging in an existing user.

## Core
This is the main method of the program, where the user will interact with the virtual Marketplace. The user is prompted to either signup as a new user or login as an existing user. If the user is a Customer, they are asked whether they want to open the full marketplace, search for a product, view their cart, view their purchase history, or logout. If they are a Seller, they are asked whether they want to view their stores or logout. 

## Customer
This class extends the User class. While having all the User class' methods inherited, it also contains two new variables, shoppingCart of type ShoppingCart, and an ArrayList< Sale > of purchases. It also contains a method that pushes all the customer's purchases to a file so the data is still retrievable after a session ends. Finally, the Customer has the option to export their purchase history into a .txt format file.
## DataException
A DataException is thrown when there is an error in the saving of a Customer or Seller's data after the end of a session. 
## FileManager
The FileManager is a key part of the data persistence in the program - it reloads all existing data (Customers, Sellers, Products, Sales, and Stores) as a new session begins. 
## MarketPlace
This class contains all the sellers and Customers in the Application, and is responsible for the creation and deletion of Users. 
## Product
The Product class contains a product's name, description, price, and quantity available.
## Sale
This class contains any information pertaining to a sale, such as the buying Customer, the Product that was sold, the number of the Product sold, and the total cost of the sale.
## Seller
This class extends the User class. While having all the User class' methods inherited, it also contains an ArrayList< Store > of stores that the user owns. It also contains a method that pushes all the seller's products to a file so the data is still retrievable after a session ends. Finally, the Seller has the option to import and export products in their stores via a .csv file. 
## ShoppingCart
This class contains the ShoppingCart's owning Customer and the current purchases waiting to be made.
## Store
The Store class contains the name of the store, the description that the Seller has chosen to give the Store, the products in the Store, and all its Sales. 
## Test
This is a tester class that was created which contains junit tests to make sure the application works as intended. The Class contains local tests to individually test the Customer, Sale, Store, Seller, and Marketplace classes.
## User
This class creates parameters and their respective getter/setter methods that were common to both Customers and Sellers. These parameters are usernames, emails, and passwords.
