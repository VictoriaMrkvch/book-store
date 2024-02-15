 # Online Book Store 📚

This is a web application for buying books online. Users can register in our store and then
choose books by different categories, add book to the shopping cart and place an order.
Our store offers a wide range of books across various genres and authors for all age groups.

### Technologies and Tools Used:
1. Spring Boot
2. Spring Security
3. Spring Data JPA
4. Swagger
5. MySQL
6. Docker

### Controllers

1. `Authentication Controller`: Managing authentication and user registration
    - POST: /api/auth/register - register a new user
    - POST: /api/auth/login -  login and get JWT tokens
2. `Book Controller`: Managing books
    - POST: /api/books - add a new book (needs an admin role)
    - GET: /api/books - get all books
    - GET: /api/books/<id> - get book by specific id
    - PUT: /api/books/<id> - update book by specific id (needs an admin role)
    - DELETE: /api/books/<id> - delete book by specific id (needs an admin role)
3. `Category Controller`: Managing categories 
    - POST: /api/categories - add a new category (needs an admin role)
    - GET: /api/categories - get all categories
    - GET: /api/categories/<id> - get category by specific id
    - PUT: /api/categories/<id> - update category by specific id (needs an admin role)
    - DELETE: /api/categories/<id> - delete category by specific id (needs an admin role)
    - GET: /api/categories/<id>/books - get all books by specific category
4. `Shopping cart Controller`: Endpoints for managing shopping cart and cart items
    - GET: /api/cart - get shopping cart with all cart items for logged user
    - POST: /api/cart - add cart item to the shopping cart
    - PUT: /api/cart/cart-items/<id> - update quantity of books in cart item
    - DELETE: /api/cart/cart-items/<id> - delete cart item by id
5. `Order Controller`: Endpoints for managing orders and order items
    - GET: /api/orders - get all logged user's orders 
    - POST: /api/orders - place an order
    - PATCH: /api/orders/<id> - update order status
    - GET: /api/orders/<id>/items - get all items from order
    - GET: /api/orders/{orderId}/items/{id} - get order item

### How to run the project

To run this project locally, follow these steps:
1. Please make sure that you have all the required technologies installed before proceeding.
2. Clone this repository to your local machine.
3. Create a .env file and set up environment variables (e.g., database connection string, JWT secret).
4. Run Docker.
5. Run the command `docker compose up`.
6. For testing using Swagger, open your browser and enter the URL: `http://localhost:8082/api/swagger-ui/index.html#/`. 
Then register a new user and test the app.
7. For testing using Postman, open the Postman web or desktop client and enter the URL `http://localhost:8088/api/{endpoint}`.


We welcome contributions from the community. If you'd like to contribute to the project, please fork the repository,
make your changes, and submit a pull request.


