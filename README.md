# Graduation Project Backend

This repository contains the backend code for the Graduation Project. It is built using Node.js and Express.js, providing APIs and services to support the project's functionalities.

## Entity-Relationship Diagram (ERD)

Below is the ER diagram representing the database structure of this project:

![ER Diagram](https://raw.githubusercontent.com/USERNAME/REPO/main/docs/erd.png](https://github.com/15mahmoud/graduation-project-backend/blob/main/uml/ERD.png)

## Features

- **User Authentication**: Secure login and registration using JWT tokens.
- **Data Management**: CRUD operations for managing project-related data.
- **Email Notifications**: Automated email sending using predefined templates.

## Technologies Used

- **Node.js**: JavaScript runtime environment.
- **Express.js**: Web framework for Node.js.
- **MongoDB**: NoSQL database for data storage.
- **Mongoose**: Object Data Modeling (ODM) library for MongoDB and Node.js.
- **JWT**: JSON Web Tokens for secure authentication.
- **Nodemailer**: Module for sending emails from Node.js applications.

## Folder Structure

- **config/**: Configuration files for database connections and other settings.
- **controllers/**: Route handlers defining the application's business logic.
- **mail/templates/**: Email templates used for notifications.
- **middleware/**: Custom middleware functions for request processing.
- **models/**: Mongoose schemas and models representing data structures.
- **routes/**: API endpoints and route definitions.
- **utils/**: Utility functions and helpers.

## Getting Started

### Prerequisites

- Node.js installed on your machine.
- MongoDB instance running locally or accessible remotely.

### Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/15mahmoud/graduation-project-backend.git

2. **Navigate to the project directory**:

   ```bash
   cd graduation-project-backend
  
3. **Install dependencies**:

    ```bash
   npm install
    
4. **Start the server**:
 
   ```bash
   npm run dev





   
