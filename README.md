# Casino Back-End System

This is a simple casino back-end system implemented using Spring Boot, designed to handle basic operations for a slot game. The system includes functionalities to get a player's balance, process wagers, record wins, and retrieve the last ten transactions. Additionally, it supports a promotional feature where the next five wagers can be free if a specific promotion code is used.

## Features

- Get player's current balance
- Process wagers (deduct money from balance)
- Record wins (add money to balance)
- Retrieve last ten transactions for a player
- Promotional feature: Next five wagers are free with promotion code "paper"

## Requirements

- Java 11 or higher
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
