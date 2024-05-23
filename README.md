# Smart Contact Manager

## Overview

Smart Contact Manager is a web-based application designed to help users manage their contacts efficiently. It provides features to add, update, delete, and view contacts. Built with Spring Boot, it offers a robust and scalable solution for contact management.

## Features

- Add new contacts
- Edit existing contacts
- Delete contacts
- View contact details
- Search for contacts
- User authentication and authorization

## Technologies Used

- **Backend:** Spring Boot, Spring Data JPA, Spring Security
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript
- **Database:** MySQL
- **Build Tool:** Maven

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6 or higher
- MySQL database

### Installation

1. **Clone the Repository**

   ```sh
   git clone https://github.com/VishalNarsinh/SmartContactManager.git
   cd SmartContactManager
   ```

2. **Configure Database**

   - Create a MySQL database named `smartcontact`.
   - Update the database configuration in `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/smartcontact
     spring.datasource.username=yourUsername
     spring.datasource.password=yourPassword
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build the Project**

   ```sh
   mvn clean install
   ```

4. **Run the Application**
   ```sh
   mvn spring-boot:run
   ```

### Usage

- Open your browser and go to `http://localhost:8080`
- Register a new user or log in with existing credentials.
- Start managing your contacts!

## Project Structure

- `src/main/java/com/smart/config` - Configuration classes
- `src/main/java/com/smart/controller` - Controller classes handling web requests
- `src/main/java/com/smart/dao` - Data Access Object classes and interfaces
- `src/main/java/com/smart/entities` - Entity classes representing database tables
- `src/main/java/com/smart/helper` - Utility and helper classes
- `src/main/java/com/smart/service` - Service classes containing business logic

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes. Ensure your code follows the project's coding standards and includes appropriate tests.

## Contact

For any inquiries or issues, please contact Vishal Narsinh at vishalnarsinh@gmail.com
