# Tests
## Test 1
Steps:
1. Click OK
2. Sign up
3. Type "john@purdue.edu" as email
4. Type "password" as password
5. Type "johndoe" as username
6. Sign up to be a seller
7. Click ok, confirmation with "Welcome seller: johndoe" comes up
8. Click "view stores"
9. Type "AD" and click ok
10. Type "watermelon shop" and click ok
11. Click OK

Expected result of test: "Your stores" line should have "- #1 watermelon shop" after it.
Test status: Passed

Expected result of test: "small melon" should be listed as a fruit.
Test status: passed
## Test 2
Steps:
1. Click OK
2. Log in
3. Type "john@purdue.edu" as email
4. Type "password" as password
5. Confirmation page will come up saying "Welcome seller: johndoe". Click OK
6. Click "View Stores"
7. Type "1" to view watermelon store
8. Type "AD" to add new fruit
9. Type "small melon"
10. Type "fun sized" as description
11. Type "5" for price
12. Type "1" for quantity
13. Click OK after confirmation
14. Type "1"

Expected result of test: "small melon" should be listed as a fruit.
Test status: passed

## Test 3
Steps:
1. Click OK
2. Sign up
3. Type "sally@purdue.edu" as email
4. Type "boilerup" as password
5. Type "sally" as username
6. Sign up as customer
7. Confirmation will pop up. Click OK.
8. Welcome will pop up. Click OK
9. Click "search for fruit"
10. Type "melon" as search query
11. Search result of "#1 small melon" will show up.
12. Type 1 into the input to view small melon
13. Click "OK" after viewing description
14. Add to Shopping Cart
15. Type "2"
16. An error message will come up. Type "1"
17. Click yes.

Expected Result: View shopping cart button should have quantity of 1.
Test Status: Passed

## Test 4
1. Click OK
2. Log in
3. Type "john@purdue.edu" as email
4. Type "password" as password
5. Confirmation page will come up saying "Welcome seller: johndoe". Click 
6. Click "View your Carted Fruits"

Expected Result: Pop-up should say "All carted items: - Customer: sally --Item: small melon (#Held: 1)---- Description: fun sized" ----- Price: 5.00 -----Quantity Avalable: 0"
Test status: Passed

## Test 5
1. Click OK
2. Log in
3. Type "sally@purdue.edu" as email
4. Type "boilerup" as password
5. Confirmation should pop up.
6. Click "view shopping cart"
7. Click "OK"
8. Type "CH"
9. Click "Yes"
10. Click "OK"
11. Logout and Quit

Expected Result: Fruit has been checked out
Test Status: passed

## Test 6
1. Click OK
2. Log in
3. Type "john@purdue.edu" as email
4. Type "password" as password
5. Click OK
6. View Statistics Dashboard
7. View Sales Statistics

Expected Result: Pop-up should say "Your Stores: - watermelon shop, products: - small melon (1 sold)"
Test status: Passed
