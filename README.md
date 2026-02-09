Вот профессиональный перевод твоего файла README.md на английский язык. Этот вариант идеально подходит для GitHub и будет отлично смотреться в глазах зарубежных рекрутеров.

🎵 SoundCloud Clone
A full-stack SoundCloud clone developed using Java Spring Boot (backend) and React (frontend).

📋 Features
✅ User registration and authentication (JWT)

✅ Track uploads (MP3, WAV)

✅ Audio playback with waveform visualization (WaveSurfer.js)

✅ Track likes and social interactions

✅ Timestamped comments

✅ Search functionality by tracks and genres

✅ Trending tracks section

✅ Fully responsive design

🛠️ Tech Stack
Backend
Java 17

Spring Boot 3.2

Spring Security + JWT

Spring Data JPA

PostgreSQL

Redis (Caching)

Maven

Frontend
React 18

React Router

Axios

WaveSurfer.js

Tailwind CSS

React Icons

🚀 Quick Start
Option 1: Using Docker (Recommended)
Bash

# Clone the repository
cd soundcloud-clone

# Start all services
docker-compose up -d --build
Application will be available at:

Frontend: http://localhost:3000

Backend API: http://localhost:8080/api

Option 2: Local Development
Prerequisites
Java 17+

Node.js 18+

PostgreSQL 15+

Redis 7+

Maven 3.8+

Backend Setup
Bash

cd backend
# Create the database
createdb soundcloud
# Run the application
mvn spring-boot:run
Frontend Setup
Bash

cd frontend
# Install dependencies
npm install
# Start dev server
npm start
📁 Project Structure
soundcloud-clone/
├── backend/
│   ├── src/main/java/com/soundcloud/
│   │   ├── config/          # Spring Configuration
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── exception/       # Global Exception Handling
│   │   ├── model/           # JPA Entities
│   │   ├── repository/      # Spring Data Repositories
│   │   ├── security/        # JWT & Security Logic
│   │   └── service/         # Business Logic
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── components/      # React Components
│   │   ├── pages/           # Page Views
│   │   ├── services/        # API Integration
│   │   └── App.js
│   └── Dockerfile
└── docker-compose.yml       # Docker Orchestration
🔑 Key API Endpoints
Authentication
POST /api/auth/register - User registration

POST /api/auth/login - User login

Tracks
GET /api/tracks - Fetch all tracks

POST /api/tracks - Upload a new track

GET /api/tracks/{id}/stream - Audio streaming

💾 Database Schema
The schema is automatically generated on startup via hibernate.ddl-auto=update.

Core Tables:

users - User profiles and credentials

tracks - Metadata for uploaded music

follows - User-to-user social relations

likes / comments - User engagement data

🔧 Configuration
Backend Environment Variables
SPRING_DATASOURCE_URL: PostgreSQL connection string

SPRING_DATA_REDIS_HOST: Redis host address

JWT_SECRET: Secret key for token generation

File Storage
By default, files are stored locally in the ./uploads directory, which is mapped as a Docker volume for persistence.

Created as a pet project for portfolio purposes.
