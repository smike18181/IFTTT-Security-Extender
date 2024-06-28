# IFTTT Applet Security Control Project

## Overview

Welcome to the IFTTT Applet Security Control Project! This project enhances the security of applets created using the IFTTT (If This Then That) platform. It features a Spring Boot backend and a React frontend, providing a seamless interface for monitoring and managing applet security.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Real-time Monitoring**: Continuously monitor applet security status.
- **Detailed Reports**: Generate comprehensive security reports.
- **User-friendly Interface**: Intuitive React-based frontend.
- **Secure Backend**: Spring Boot backend ensures secure data handling.
- **Custom Alerts**: Set up custom alerts for security breaches.

## Architecture

The project consists of:

1. **Backend (Server)**
   - Built with Spring Boot
   - Handles API requests and security checks

2. **Frontend (Client)**
   - Developed using React
   - Provides interactive user interface

## Getting Started

### Prerequisites

- **Node.js** and **npm**: [Download and install](https://nodejs.org/)
- **Java**: JDK 11 or higher [Download and install](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **IntelliJ IDEA**: Recommended IDE [Download and install](https://www.jetbrains.com/idea/)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/smike18181/ifttt-applet-security-control.git
   cd ifttt-applet-security-control

## Backend Setup

1. **Open the Project in IntelliJ IDEA**
   - Launch IntelliJ IDEA and open the root directory of the cloned `ifttt-applet-security-control` project.

2. **Configure Dependencies**
   - Navigate to the `server` directory within IntelliJ IDEA.
   - Ensure that your IDE is configured to recognize the project as a Maven or Gradle project.
   - Update your `pom.xml` (Maven) or `build.gradle` (Gradle) file to include the necessary dependencies for your Spring Boot application.

3. **Database Configuration**
   - Open the `src/main/resources/application.properties` file.
   - Configure your database settings according to your requirements. For example:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
     spring.datasource.username=your_username
     spring.datasource.password=your_password
     spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
     spring.jpa.hibernate.ddl-auto=update
     ```
   - Replace `your_database_name`, `your_username`, and `your_password` with your actual database credentials.

4. **Run the Backend Application**
   - Find the main class of your Spring Boot application (usually `ServerApplication.java`) within IntelliJ IDEA.
   - Right-click on the main class and select "Run" to start the backend server.
   - Ensure that the backend server starts without any errors.

## Frontend Setup

### Prerequisites

Before setting up the frontend, ensure you have Node.js and npm installed on your system. If not, download and install them from [nodejs.org](https://nodejs.org/).

### Installation

1. **Navigate to the Frontend Directory**
   - Open a new terminal or command prompt.
   - Change directory to the `client` folder within your project:
     ```bash
     cd client
     ```

2. **Install Dependencies**
   - Once inside the `client` directory, install all necessary dependencies using npm:
     ```bash
     npm install
     ```

### Running the Application

1. **Start the Frontend Development Server**
   - After installing dependencies, start the frontend development server:
     ```bash
     npm start
     ```
   - This command will launch the React application in development mode. 

2. **Accessing the Application**
   - Open your web browser and go to `http://localhost:3000` to access the frontend application.
   - You should see the dashboard interface where you can monitor and manage the security of your IFTTT applets.

## Usage

Use the dashboard provided by the frontend interface to view real-time security information, generate reports, and set up custom alerts for your IFTTT applets.

## Contributing

We welcome contributions from the community! If you'd like to contribute, please fork the repository, create a new branch, and submit a pull request. For major changes, please open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

