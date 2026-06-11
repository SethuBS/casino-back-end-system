# Casino Back End System

This is a simple casino back-end system implemented using Spring Boot, designed to handle basic operations for a slot
game. The system includes functionalities to get a player's balance, process wagers, record wins, and retrieve the last
configured number of transactions. Additionally, it supports a promotional feature driven by a configured promotion
code.

## Features

- Get player's current balance
- Process wagers (deduct money from balance)
- Record wins (add money to balance)
- Retrieve recent transactions for a player
- Promotional feature: wagers can be free when a player has free wagers and uses the configured promotion code

## Requirements

- Java 17 or higher
- Maven

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
   ```

2. **Build the project**

```bash
mvn clean install
```

3. **Run the Application**

```powershell
$env:CASINO_TRANSACTION_HISTORY_PASSWORD="<your-password>"
$env:CASINO_PROMOTION_FREE_WAGER_CODE="<your-promotion-code>"
mvn spring-boot:run
```

4. **Access the H2 console**

The H2 console path, server port, and datasource URL are configurable in `application.yml`.

Datasource URL: configured by `CASINO_DATASOURCE_URL`
User Name: configured by `CASINO_DATASOURCE_USERNAME`
Password: configured by `CASINO_DATASOURCE_PASSWORD`

## Configuration

Runtime settings are managed through `src/main/resources/application.yml` and can be overridden with environment
variables:

- `CASINO_TRANSACTION_HISTORY_PASSWORD`: password required by the transaction-history endpoint
- `CASINO_PROMOTION_FREE_WAGER_CODE`: promotion code accepted for free wagers
- `CASINO_TRANSACTION_HISTORY_LIMIT`: maximum transactions returned by the history endpoint
- `CASINO_API_BASE_PATH`: base API path
- `CASINO_DATASOURCE_URL`, `CASINO_DATASOURCE_USERNAME`, `CASINO_DATASOURCE_PASSWORD`: datasource settings
- `CASINO_DEMO_DATA_ENABLED` and `CASINO_DEMO_*`: local demo seed data settings

## Curl Smoke Tests

Run the application first, then execute the full block from a separate PowerShell terminal. It prompts for values that
are not already available as environment variables.

```powershell
function Require-Value($name, $prompt)
{
    $value = [Environment]::GetEnvironmentVariable($name)
    if ([string]::IsNullOrWhiteSpace($value))
    {
        $value = Read-Host $prompt
    }
    return $value
}

$baseUrl = (Require-Value "CASINO_TEST_BASE_URL" "Base API URL").TrimEnd("/")
$password = Require-Value "CASINO_TRANSACTION_HISTORY_PASSWORD" "Transaction history password"
$promotionCode = Require-Value "CASINO_PROMOTION_FREE_WAGER_CODE" "Promotion code"
$playerId = [long](Require-Value "CASINO_TEST_PLAYER_ID" "Player ID")
$username = Require-Value "CASINO_TEST_USERNAME" "Player username"
$wagerAmount = [decimal](Require-Value "CASINO_TEST_WAGER_AMOUNT" "Wager amount")
$winAmount = [decimal](Require-Value "CASINO_TEST_WIN_AMOUNT" "Win amount")
$runId = [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds()

curl.exe -s "$baseUrl/balance/$playerId"

$wagerBody = @{
  transactionId = "wager-smoke-$runId"
  playerId = $playerId
  amount = $wagerAmount
  promotionCode = $promotionCode
} | ConvertTo-Json -Compress

curl.exe -s -X POST "$baseUrl/wager" -H "Content-Type: application/json" -d $wagerBody

$winBody = @{
  transactionId = "win-smoke-$runId"
  playerId = $playerId
  amount = $winAmount
} | ConvertTo-Json -Compress

curl.exe -s -X POST "$baseUrl/win" -H "Content-Type: application/json" -d $winBody

$transactionsBody = @{
  username = $username
  password = $password
} | ConvertTo-Json -Compress

curl.exe -s -X POST "$baseUrl/transactions" -H "Content-Type: application/json" -d $transactionsBody
```

# API Endpoints

**Get Player Balance**
**Endpoint:** ***`GET /casino/balance/{playerId}`***

**Response:**

- ***`200 OK`*** with the player's balance:

```json
{
  "playerId": 1,
  "balance": 1000.00
}
```

- ***`400 Bad Request`*** if the player does not exist

# Process Wager

**Endpoint:** `POST /casino/wager`

**Request Body:**

```json
{
  "transactionId": "unique-transaction-id",
  "playerId": 1,
  "amount": 100,
  "promotionCode": "<configured-promotion-code>"
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

**Get Recent Transactions**
***Endpoint: `POST /casino/transactions`***

**Request Body:**

```json
{
  "username": "playerUsername",
  "password": "<configured-password>"
}
```

**Response:**

- ***`200 OK`*** with the configured number of recent transactions

```json
[
  {
    "transactionId": "unique-transaction-id",
    "playerId": 1,
    "amount": 50.00,
    "type": "win",
    "timestamp": "2026-06-11T08:30:00"
  }
]
```

- ***`400 Bad Request`*** if the player does not exist
- ***`401 Unauthorized`*** if the password is incorrect

# Additional Information

- The system uses an H2 in-memory database, so data will be lost when the application stops.
- Promotion behavior and demo seed data are configurable through environment-backed YAML properties.

# Contributing

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

# License

This project is licensed under the MIT License. See the [MIT License](LICENSE) file for details.
