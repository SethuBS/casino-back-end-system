# Casino Back-End System

This is a simple casino back-end system implemented using Spring Boot, designed to handle basic operations for a slot game. The system includes functionalities to get a player's balance, process wagers, record wins, and retrieve the last ten transactions. Additionally, it supports a promotional feature where the next five wagers can be free if a specific promotion code is used.

## Features

- Get player's current balance
- Process wagers (deduct money from balance)
- Record wins (add money to balance)
- Retrieve last ten transactions for a player
- Promotional feature: Next five wagers are free with promotion code "paper"

## Requirements

- Java 17 or higher
- Maven or Gradle for build automation

## Dependencies

- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database

## Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/SethuBS/casino-back-end-system.git
   cd casino-backend
   
2. **Build the project**
 ```bash
   mvn clean install
```
3. **Run the Application**
```bash
mvn spring-boot:run
```
4. **Access the H2 console**

The H2 console is accessible at `http://localhost:8080/h2-console.`

JDBC URL: `jdbc:h2:mem:casino`
User Name: `sa`
Password: (leave empty)

# API Endpoints

**Get Player Balance**
**Endpoint:** ***`GET /casino/balance/{playerId}`***

**Response:**

- ***`200 OK`*** with the player's balance
- ***`400 Bad Request`*** if the player does not exist
# Process Wager
**Endpoint:** `POST /casino/wager`

**Request Body:**
```json
{
  "transactionId": "unique-transaction-id",
  "playerId": 1,
  "amount": 100,
  "promotionCode": "paper"
}
```

**Response:**

- ***`200 OK`*** if the wager is processed successfully
- ***`400 Bad Request`*** if the player does not exist
- ***`418 I'm a teapot`*** if the player has insufficient funds

**Process Win**
***Endpoint: `POST /casino/win`***

**Request Body:**
```json
{
  "transactionId": "unique-transaction-id",
  "playerId": 1,
  "amount": 150
}
```

**Response:**

- ***`200 OK`*** if the win is recorded successfully
- ***`400 Bad Request`*** if the player does not exist

**Get Last Ten Transactions**
***Endpoint: `POST /casino/transactions`***

**Request Body:**
```json
{
  "username": "playerUsername",
  "password": "swordfish"
}
```

**Response:**

- ***`200 OK`*** with the list of last ten transactions
- ***`400 Bad Request`*** if the player does not exist
- ***`401 Unauthorized`*** if the password is incorrect

# Additional Information

- The system uses an H2 in-memory database, so data will be lost when the application stops.
- The promotion feature allows the next five wagers to be free if the promotion code "paper" is used in the wager request.
  
# Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

# License
This project is licensed under the MIT License. See the [MIT License](LICENSE) file for details.
