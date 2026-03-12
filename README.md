# NEURA: Backend Persistence Service

This is the Spring Boot service that handles user authentication and history tracking for the NEURA suite.

## 🚀 Running the Server
### Local Development
1. Ensure Java 17+ is installed.
2. Run using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

### Deployment (Render/Docker)
The project includes a `Dockerfile` and `Procfile` for easy deployment.
- **Docker**: `docker build -t ai-ocr-backend .`
- **Render**: Automatically detected via `Procfile` or `Dockerfile`.

## 🛠️ API Endpoints
- `POST /api/users/register`: User signup.
- `POST /api/users/login`: User auth.
- `POST /api/history/save`: Persistence for AI logs.
- `GET /api/history/{username}`: Retrieval of user activity.

## 📊 Database
- Uses **PostgreSQL** for production persistence.
- Configurable via environment variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
