# OTP-Based Authentication System

## Overview

This project implements a secure OTP (One-Time Password) based authentication system, allowing users to register and log in using their mobile numbers. It uses RESTful APIs for user registration, login, and OTP management, ensuring secure access to the system.

## Features

- **User Registration**: Users can register by providing their name, mobile number, address, and date of birth.
- **OTP Generation**: Once registered, users can request an OTP, which is sent to their mobile number for login.
- **OTP Validation**: Users can validate the OTP to gain access to the system.
- **Device Fingerprint**: Device fingerprinting is used to track user devices for enhanced security.
- **Login via OTP**: Users can log in using their mobile number and receive an OTP for authentication.
- **Resend OTP**: Users can request to resend the OTP if it wasn’t received or expired.

## Technologies Used

- **Backend**: Java 17, Spring Boot 3
- **Database**: MySQL 8
- **OTP Service**: Twilio (for sending OTP)
- **Custom OTP Logic**: Custom OTP generation logic implemented in the backend
- **Security**: Device fingerprinting for enhanced user security

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/riteshgaigawali/otp_based_auth_system.git
   ```

2. Navigate to the project directory:

   ```bash
   cd otp_based_auth_system
   ```

3. Set up your MySQL database and update the database configurations in the `application.properties` file:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/otp_auth_db
   spring.datasource.username=root
   spring.datasource.password=root
   ```

4. Install the necessary dependencies via Maven:

   ```bash
   mvn clean install
   ```

5. Run the application:

   ```bash
   mvn spring-boot:run
   ```

6. The application will start on `http://localhost:8080`.

## API Documentation

### 1. User Registration

- **Endpoint**: `POST /api/register`
- **Description**: Registers a new user by providing their name, mobile number, address, and date of birth. The system will generate a unique device fingerprint for each user during registration.

#### Request Body:

```json
{
  "name": "John Doe",
  "mobileNumber": "+14155552671",
  "address": "123 Main Street, New York, NY",
  "dob": "1990-10-15"
}
```

#### Response:

```json
{
  "id": 10,
  "name": "John Doe",
  "mobileNumber": "+14155552671",
  "address": "123 Main Street, New York, NY",
  "dob": "1990-10-15",
  "createdAt": "2024-11-17T16:00:45.123456",
  "deviceFingerprint": "d21a7d8f214cb451bc03b72ac34ff342"
}
```

- **Status Codes**:
  - `200 OK`: Successfully registered the user.
  - `400 Bad Request`: Invalid input data.
  - `500 Internal Server Error`: Server error during registration.

### 2. OTP Request

- **Endpoint**: `POST /api/request-otp`
- **Description**: Generates and sends an OTP to the mobile number associated with the user’s account.

#### Request Body:

```json
{
  "mobileNumber": "+14155552671"
}
```

#### Response:

```json
{
  "message": "OTP has been sent to +14155552671."
}
```

- **Status Codes**:
  - `200 OK`: OTP sent successfully.
  - `400 Bad Request`: Invalid mobile number.
  - `500 Internal Server Error`: Server error while generating OTP.

### 3. OTP Validation

- **Endpoint**: `POST /api/auth/validate-otp`
- **Description**: Validates the OTP entered by the user to allow login.

#### Request Body (Content-Type: `x-www-form-urlencoded`):

- **Key**: `mobileNumber`
- **Value**: `+14155552671`
- **Key**: `otp`
- **Value**: `823745`

#### Response:

```json
{
  "message": "Invalid OTP !"
}
```

or

```json
{
  "message": "Log In Successful !"
}
```

- **Status Codes**:
  - `200 OK`: OTP validated successfully (User logged in).
  - `400 Bad Request`: Invalid OTP or mobile number.
  - `401 Unauthorized`: OTP expired or invalid.
  - `500 Internal Server Error`: Server error during OTP validation.

### 4. User Login (OTP Request)

- **Endpoint**: `POST /api/auth/login`
- **Description**: Allows users to log in by providing their mobile number. If an OTP has already been sent, it will inform the user; otherwise, an OTP will be sent to the provided number.

#### Request Body (Content-Type: `x-www-form-urlencoded`):

- **Key**: `mobileNumber`
- **Value**: `+14155552671`

#### Response:

```json
{
  "message": "Otp already sent, if not received try resend-otp !"
}
```

or

```json
{
  "message": "Otp sent to : +14155552671"
}
```

- **Status Codes**:
  - `200 OK`: OTP sent or already sent to the provided number.
  - `400 Bad Request`: Invalid mobile number.
  - `500 Internal Server Error`: Server error during OTP request.

### 5. Get User Details

- **Endpoint**: `GET /api/auth/user/{mobileNumber}`
- **Description**: Retrieves the user details for the specified mobile number. This endpoint returns the user’s information if the mobile number exists in the system.

#### Request:

No request body is required for this endpoint.

#### Example URL:

```
GET http://localhost:8080/api/auth/user/+14155552671
```

#### Response:

```json
{
  "id": 10,
  "name": "John Doe",
  "mobileNumber": "+14155552671",
  "address": "123 Main Street, New York, NY",
  "dob": "1990-10-15",
  "createdAt": "2024-11-17T16:00:45.123456",
  "deviceFingerprint": "d21a7d8f214cb451bc03b72ac34ff342"
}
```

- **Status Codes**:
  - `200 OK`: User details retrieved successfully.
  - `404 Not Found`: User not found with the given mobile number.
  - `500 Internal Server Error`: Server error during the request.

### 6. Resend OTP

- **Endpoint**: `POST /api/auth/resend-otp`
- **Description**: Resends the OTP to the specified mobile number. If no OTP exists for the given mobile number, the system will notify the user.

#### Request Body (Content-Type: `x-www-form-urlencoded`):

- **Key**: `mobileNumber`
- **Value**: `+14155552671`

#### Response:

```json
{
  "message": "New Otp sent to +14155552671."
}
```

or

```json
{
  "message": "There is no existing OTP for +14155552671."
}
```

- **Status Codes**:
  - `200 OK`: OTP resent successfully.
  - `400 Bad Request`: Invalid mobile number or no existing OTP.
  - `500 Internal Server Error`: Server error during OTP resend.

## Testing

To test the API endpoints, you can use tools like [Postman](https://www.postman.com/) or [cURL].

Example of testing user registration with Postman:

- Set the request type to `POST` and enter the URL `http://localhost:8080/api/register`.
- In the body, provide the necessary user data as a JSON object.
- Send the request and check the response for successful registration.

Example of testing user login (OTP request) with Postman:

- Set the request type to `POST` and enter the URL `http://localhost:8080/api/auth/login`.
- Set the `Content-Type` to `application/x-www-form-urlencoded`.
- Provide the `mobileNumber` in the body.
- Send the request and check the response for OTP status.

Example of testing OTP validation with Postman:

- Set the request type to `POST` and enter the URL `http://localhost:8080/api/auth/validate-otp`.
- Set the `Content-Type` to `application/x-www-form-urlencoded`.
- Provide the `mobileNumber` and `otp` in the body.
- Send the request and check the response for OTP validation result.

Example of testing user details retrieval with Postman:

- Set the request type to `GET` and enter the URL `http://localhost:8080/api/auth/user/+14155552671`.
- Send the request and check the response for the user’s details.

Example of testing OTP resend with Postman:

- Set the request type to `POST` and enter the URL `http://localhost:8080/api/auth/resend-otp`.
- Set the `Content-Type` to `application/x-www-form-urlencoded`.
- Provide the `mobileNumber` in the body.
- Send the request and check the response for OTP resend status.

## Credits

Twilio: This project leverages Twilio’s SMS service for secure and reliable OTP delivery. For more information, visit Twilio's official website.

## Contact

If you have any questions

or feedback, feel free to reach out to me at:

- **Email**: [gaygawaliritesh@gmail.com](mailto:gaygawaliritesh@gmail.com)

---
