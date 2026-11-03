# 🏥 Hospital Management System - Backend

A comprehensive Hospital Management System backend built with **Spring Boot** and **Java**, providing RESTful APIs for managing patients, doctors, appointments, billing, prescriptions, lab tests, and medical records.

---

## 🚀 Live Demo

- **Backend API:** [https://sk-hospital-management.onrender.com](https://sk-hospital-management.onrender.com)
- **Frontend Application:** [https://hospital-management-frontend-xi.vercel.app](https://hospital-management-frontend-xi.vercel.app)

---

## 📋 Features

### Core Modules
- 👨‍⚕️ **Doctor Management** - Add, update, view, and manage doctor profiles
- 👤 **Patient Management** - Complete patient information and medical history
- 📅 **Appointment System** - Schedule and manage appointments
- 💊 **Prescription Management** - Create and track prescriptions
- 🧪 **Lab Tests** - Order and manage laboratory tests
- 💰 **Billing System** - Generate invoices and manage payments
- 📊 **Dashboard** - Real-time statistics and insights
- 📈 **Reports** - Generate various medical and administrative reports

### Authentication & Authorization
- 🔐 Role-based access control (Admin, Doctor, Patient)
- 🔑 Secure password management
- 👥 User profile management
- 🆕 First-time login password change enforcement

---

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Database:** MySQL (TiDB Cloud)
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Deployment:** Docker (Render)
- **API Documentation:** REST APIs

---

## 📦 Dependencies

```xml
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- MySQL Connector
- Lombok (optional)
- Spring Boot DevTools
```

---

## 🔧 Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

### Local Development Setup

1. **Clone the repository**
```bash
git clone https://github.com/DopatiSudheer/hospital-management-backend.git
cd hospital-management-backend
```

2. **Configure Database**

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.admin.email=admin@hospital.com
app.admin.password=admin@123
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

---

## 🐳 Docker Deployment

### Using Docker

```dockerfile
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Build and Run
```bash
docker build -t hospital-backend .
docker run -p 8080:8080 hospital-backend
```

---

## 🌐 API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/register` | User registration |
| POST | `/api/auth/change-password` | Change password |

### Patients
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/patients` | Get all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| POST | `/api/patients` | Create new patient |
| PUT | `/api/patients/{id}` | Update patient |
| DELETE | `/api/patients/{id}` | Delete patient |

### Doctors
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/doctors` | Get all doctors |
| GET | `/api/doctors/{id}` | Get doctor by ID |
| POST | `/api/doctors` | Create new doctor |
| PUT | `/api/doctors/{id}` | Update doctor |
| DELETE | `/api/doctors/{id}` | Delete doctor |

### Appointments
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/appointments` | Get all appointments |
| GET | `/api/appointments/{id}` | Get appointment by ID |
| POST | `/api/appointments` | Create appointment |
| PUT | `/api/appointments/{id}` | Update appointment |
| DELETE | `/api/appointments/{id}` | Cancel appointment |

### Additional Endpoints
- `/api/billing` - Billing management
- `/api/prescriptions` - Prescription management
- `/api/lab-tests` - Lab test management
- `/api/medicines` - Medicine inventory
- `/api/dashboard` - Dashboard statistics
- `/api/reports` - Generate reports

---

## 🔐 Default Credentials

**Admin Account:**
- Email: `admin@hospital.com`
- Password: `admin@123`

**Note:** Admin account is automatically created on first application startup.

---

## 🔑 Password Generation Logic

When admin creates a new doctor or patient:

**Format:** `Name@Last4DigitsOfPhone`

**Example:**
- Doctor: Dr. Sanjay, Phone: 9701126520 → Password: `Sanjay@6520`
- Patient: Mr. Kumar, Phone: 9876543210 → Password: `Kumar@3210`

**Features:**
- Automatically removes title prefixes (Dr., Mr., Ms., Mrs.)
- Users must change password on first login
- Secure password validation

---

## 🗄️ Database Schema

### Main Tables
- `users` - User authentication and roles
- `patients` - Patient information
- `doctors` - Doctor profiles
- `appointments` - Appointment scheduling
- `prescriptions` - Prescription records
- `lab_tests` - Laboratory tests
- `billing` - Billing and invoices
- `medicines` - Medicine inventory
- `medical_records` - Patient medical history

---

## 🌍 Environment Variables

For production deployment, configure these environment variables:

```bash
DATABASE_URL=jdbc:mysql://your-db-host:3306/hospital?sslMode=VERIFY_IDENTITY
DATABASE_USERNAME=your_username
DATABASE_PASSWORD=your_password
ADMIN_PASSWORD=your_admin_password
SERVER_PORT=8080
```

---

## 📊 Project Structure

```
hospital-management-backend/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/management/
│   │   │   ├── config/          # Security & configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── model/           # Entity classes
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── service/         # Business logic
│   │   │   ├── dto/             # Data transfer objects
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/                    # Unit tests
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🚀 Deployment

### Deployed on Render.com

**Automatic Deployment:**
- Push to `main` branch on GitHub
- Render automatically builds and deploys
- Zero downtime deployments

**Deployment URL:** [https://sk-hospital-management.onrender.com](https://sk-hospital-management.onrender.com)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 👨‍💻 Developer

**Sudheer Kumar Dopati**

- GitHub: [@DopatiSudheer](https://github.com/DopatiSudheer)
- Project: Hospital Management System
- Year: 2024

---

## 🐛 Known Issues

- None currently reported

---

## 📞 Support

For issues or questions:
- Open an issue on GitHub
- Contact: [Your Email/Contact]

---

## 🎯 Roadmap

- [ ] Add email notifications
- [ ] Implement appointment reminders
- [ ] Add file upload for medical reports
- [ ] Integrate payment gateway
- [ ] Add real-time chat support
- [ ] Mobile app integration

---

## ⭐ Acknowledgments

- Spring Boot Team
- MySQL Community
- TiDB Cloud
- Render.com

---

**Made with ❤️ for better healthcare management**
