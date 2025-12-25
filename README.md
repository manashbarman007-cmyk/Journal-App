# 📓 JournalApp

JournalApp is a Spring Boot application that integrates with MongoDB, Redis, Kafka, Google OAuth2, Swagger for API documentation, and Logback for logging. It also supports email notifications via Gmail SMTP.

---

## 🚀 Features
- **MongoDB Integration**: Stores journal entries in a cloud-hosted MongoDB database.
- **Automatic Index Creation**: Ensures efficient query performance.
- **Weather API Support**: Fetches weather data using an external API key.
- **Email Notifications**: Configured with Gmail SMTP for sending emails every week.
- **Redis Caching**: Provides caching support for faster performance.
- **Kafka Messaging**: Enables asynchronous communication using Kafka.
- **Google OAuth2 Authentication**: Allows secure login with Google accounts.
- **Swagger UI**: Interactive API documentation and testing.
- **Logback Logging**: Centralized and configurable logging framework.

---

## ⚙️ Configuration

The application uses environment variables for sensitive information.  

### Environment Variables
All required variables are listed in the `.env.example` file.  
To set up your environment:

1. Copy `.env.example` to `.env`:

   ```bash
   cp .env.example .env
   ```
   
---

### MongoDB Atlas

This project uses **MongoDB Atlas** (cloud-hosted MongoDB).  
- The connection string is defined in `.env` using:
  ```env
  EMAIL_ID=your-mongo-username
  PASSWORD=your-mongo-password

---


## 🔑 Google OAuth2 Setup

To enable Google login, you need to configure OAuth2 credentials:

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create OAuth2 credentials (Client ID and Client Secret).
3. Add them to your `.env` file:
   ```env
   CLIENT_ID=your-google-client-id
   CLIENT_SECRET=your-google-client-secret
4. Get the authorization code from the Google OAuth Playground (https://developers.google.com/oauthplayground).
   - Select the required scopes (e.g., email, profile).
   - Authorize APIs and exchange the authorization code for tokens.
5. Once authorized, the application will handle the callback at: http://localhost:8080/auth/google/callback
6. The callback will provide the token and finally a JWT in the URL

---

## 📖 API Documentation (Swagger)

Swagger UI is enabled for interactive API exploration and testing.

- Once the application is running locally, you can access it at: http://localhost:8080/swagger-ui/index.html

---

## 🐳 Running with Docker Compose

To start the application and its dependencies using Docker Compose, run:

```bash
docker-compose up --build
```
---

## 👨‍💻 Author
**Manash Barman**  
Backend Developer | Java, Spring Boot, Microservices  
[LinkedIn](https://www.linkedin.com/in/manash-barman-15b1833a1/) | [GitHub](https://github.com/manashbarman007-cmyk)

---
