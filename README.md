# 🛒 Full Stack Ecommerce Platform

A modern and fully functional **Ecommerce Web Application** built with **Spring Boot (Java)** in the backend and **React.js** in the frontend. This platform supports **user authentication, product browsing, cart management, order placement**, and an **admin dashboard** with powerful controls and analytics.

---

## 🚀 Tech Stack

### 🧩 Backend:
- Java 8
- Spring Boot
- Spring Security + JWT
- RESTful API
- MySQL
- Swagger UI

### 🎨 Frontend:
- React.js (Hooks)
- Tailwind CSS
- Axios
- React Router
- Toastify (Notifications)
- Lottie Animations
- Responsive Design + Dark Mode

---

## 👥 User Roles

- 👤 **Customer**  
  Can register/login, browse products, add to cart, place orders, cancel orders, and download invoices.

- 🛠️ **Admin**  
  Has full control: manage products, stock, users, and view analytics (revenue, user count, orders).

---

## ✨ Key Features

### 🔐 Authentication
- JWT-based secure login/register
- Role-based access (Admin vs Customer)

### 🛍️ Customer Features
- Product listing with image support
- Search, pagination, dark mode toggle
- Cart operations: add, update, remove, clear
- Order placement from cart
- Cancel order (soft delete)
- PDF invoice download for each order

### ⚙️ Admin Features
- Admin dashboard with analytics
- Toggle product availability
- Update stock quantity
- Upload/update product image
- Deactivate users (prevent login)

### 🧠 UX/UI Highlights
- Fully responsive (mobile + desktop)
- Framer Motion + Lottie Animations
- Toast notifications for all actions
- Clean, recruiter-friendly design

---

## 🔧 Setup Instructions

### 📦 Backend (Spring Boot)


# Navigate to backend folder

cd ecommerce-backend

# Configure MySQL credentials in application.properties

spring.datasource.username=root
spring.datasource.password=your_password

# Run the app

./mvnw spring-boot:run


**## 💻 Frontend (React)**

bash
Copy
Edit
# Navigate to frontend

cd ecommerce-frontend

# Install dependencies

npm install

# Start the frontend

npm start


## 🔗 API Documentation

**Swagger UI**: http://localhost:8081/swagger-ui/index.html

---

## 🙏 Acknowledgements

Special thanks to the open source tools and Spring community.

----


## 🙋‍♂️ Author

**Mayank Jain**

**Backend Developer | Java + Spring Boot Enthusiast**

🔗 LinkedIn: https://www.linkedin.com/in/mayank-jain-10-/ 

🌐 Portfolio: https://mayank-jain-java-dev-ot2inpo.gamma.site/
