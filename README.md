# Expense Tracker Bot

<!-- TOC -->
* [Expense Tracker Bot](#expense-tracker-bot)
  * [Description](#description)
  * [Features](#features)
  * [Technologies used](#technologies-used)
  * [Installation](#installation)
    * [Clone](#clone)
    * [Create database](#create-database)
    * [Set up connection to database](#set-up-connection-to-database)
    * [Create bot](#create-bot)
    * [Build and run on the command line](#build-and-run-on-the-command-line)
    * [Run in IntelliJ IDEA](#run-in-intellij-idea)
    * [Use](#use)
<!-- TOC -->

## Description

This is a Telegram bot that helps you track your expenses.
You can allow access to the bot for only certain users with whom you share costs.
Expense records are saved in a local database and are always available to you from Telegram.

## Features

Bot commands:

- Add expense: `/add_expense`
- Add expense category: `/add_expense_category`
- Cancel current transaction: `/cancel_transaction`
- Show detailed expense report: `/show_expenses`
- Show summary expense report: `/show_summary_expenses`
- Show expense categories: `/show_expense_categories`
- Get started: `/start`

## Technologies used

- Java 17
- Hibernate
- PostgeSQL
- Spring Boot
- Spring Data JPA
- Maven
- Lombok
- Liquibase
- [Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots)
that uses [Telegram bot API](https://core.telegram.org/bots) 

## Installation

### Clone

    git clone https://github.com/maxlutovinov/expense-tracker-bot.git

> **Note:**  
> All the following terminal/cmd commands are executed from the root directory of the project.

### Create database

- Install PostgreSQL
- Create a database with the name you specify below in the `spring.datasource.url` property

### Set up connection to database

Change the database properties to yours in [application.properties](src/main/resources/application.properties) file.<br>
Example for PostgreSQL database:

    spring.datasource.url=jdbc:postgresql://localhost:5432/expenses
    spring.datasource.username=postgres
    spring.datasource.password=12345678

### Create bot

- Create a bot with BotFather (`t.me/BotFather`)
- Specify `bot.token` and `bot.username` in [application.properties](src/main/resources/application.properties)
- In the `bot.authorized-users` property, specify a list of Telegram identifiers of bot users who are allowed access 
to the bot.

### Build and run on the command line

    ./mvnw package

If this fails, install Maven and execute the following build command:

    mvn package

Then run the jar:

    java -jar target/expense-tracker-bot-0.0.1-SNAPSHOT.jar

### Run in IntelliJ IDEA

Alternatively, open the project in IntelliJ IDEA and run 
[ExpenseTrackerBotApplication](src/main/java/telegram/expensetrackerbot/ExpenseTrackerBotApplication.java).

### Use
Once you launch the app, go to your Telegram bot and send it commands.
