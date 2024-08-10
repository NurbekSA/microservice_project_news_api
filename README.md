# Microservice Project: News API

## Overview

This project is a news service based on microservice.

## Features

- **Create Different Types of News**: 
  - **Notification News**: Short and urgent news notifications.
  - **Template-Based News**: Standard news articles built using predefined templates.
  - **Block-Based News**: Flexible news articles that allow adding images, text, and various styles.
- **Filter by Category**: Allows filtering of news based on categories like Technology, Sports, Health, etc.
- **Search Functionality**: Search for news articles by keywords.
- **Scalability**: Built using microservices architecture for easy scaling.
- **Documentation**: Fully documented API endpoints for easy integration.

## Technologies Used

- **Programming Language**: [ Scala ]
- **Database**: [ MySQL ]
- **Messaging**: [ RabbitMQ ]
- **Containerization**: Docker
- **CI/CD**: GitHub Actions for continuous integration and deployment.

## Architecture

The project follows a microservices architecture where each service is responsible for a specific functionality. The services communicate with each other through RESTful APIs.

### Services

1. **Article Service**: Manages and retrieves news articles.
2. **User Service**: Handles user authentication and authorization.
3. **Search Service**: Provides search capabilities across the news articles.
4. **Category Service**: Manages different news categories.

### Database

The project uses [PostgreSQL] as the primary database to store news articles, user data, and other relevant information.

## Installation

To run this project locally, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/NurbekSA/microservice_project_news_api.git
   ```
2. **Navigate to the project directory:**
   ```bash
   cd microservice_project_news_api
   ```
3. **Build the project using Docker Compose:**
   ```bash
   docker-compose up --build
   ```
4. **Access the API:**
   - The API will be available at `http://localhost:8080`



