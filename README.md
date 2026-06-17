# ID Card Management System (DevOps Final Project)

A full-featured **Spring Boot 3.5** web application for managing student, employee, and user ID cards.

## Student Information
* **Name**: La Vireak
* **Email**: e20251458@gic.itc.edu.kh
* **Class**: GIC, Institute of Technology of Cambodia (ITC)
* **Course**: DevOps Final Project

---

## Features

| Feature | Description |
| :--- | :--- |
| **CRUD Profiles** | Complete create, read, update, and delete operations for user profiles. |
| **Photo Upload** | Accept JPEG/PNG (max 5 MB), validated and stored on the local filesystem. |
| **ID Card Template Engine** | Custom Thymeleaf templates with color configuration (primary, secondary, text colors, organization name, layout). |
| **Live Preview** | Instant visual render of the HTML ID card layout with custom colors, barcode, and QR code. |
| **Unique ID Generation** | Generates a sequential `YYYY-DEPT-###` registration number (e.g., `2026-ENG-001`) and a UUID. |
| **PDF Export** | Export a high-resolution, credit-card sized PDF using **iText 7**. |
| **Batch Generation** | Create multiple profiles at once and export all generated PDFs inside a ZIP archive. |
| **QR Code / Verification** | Generates a ZXing QR code pointing to the live card verification URL (`/verify/{uuid}`). |
| **Barcode** | Generates a ZXing barcode supporting `CODE_128` or `EAN_13` format. |

---

## Tech Stack

* **Java 25** (LTS)
* **Spring Boot 3.5.0** (Upgraded for full Java 25 & ASM compatibility)
* **Spring Data JPA & MySQL** (Auto-creates `idcard_db` schema on startup)
* **Thymeleaf** (For HTML layouts, templates, and live preview)
* **iText 7 (v7.2.5)** (PDF document layout generation)
* **ZXing (v3.5.2)** (QR Code and Barcode creation)
* **Bootstrap 5 & Vanilla CSS** (Dark theme and modern glassmorphism design)
* *Note: Lombok was completely removed from the project to ensure compatibility with Java 25 compiler.*

---

## Setup & Running the Application

### Prerequisites
* **Java Development Kit (JDK) 25**
* **Apache Maven 3.9+**
* **MySQL Server** running on port 3306

### 1. Database Configuration
Update your MySQL database password in [application.properties](src/main/resources/application.properties):
```properties
spring.datasource.username=root
spring.datasource.password=Sadimon_99
```
*Note: The app will automatically create the database `idcard_db` if it doesn't already exist.*

### 2. Run the Application
Run the following command in the root folder of the project:
```powershell
mvn spring-boot:run
```
*(If Maven is not in your system PATH, use the direct path to the wrapper/executable):*
```powershell
D:\tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
```

Once started, open your browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

### 3. Run Tests
To execute the suite of 20 unit and integration tests (built with JUnit 5 & Mockito):
```powershell
mvn test
```
*(Or use direct path):*
```powershell
D:\tools\apache-maven-3.9.9\bin\mvn.cmd test
```

---

## REST API Reference

| Method | Path | Description |
| :--- | :--- | :--- |
| **GET** | `/api/profiles` | List all profiles (paginated) |
| **POST** | `/api/profiles` | Create a new profile |
| **GET** | `/api/profiles/{id}` | Get profile details |
| **PUT** | `/api/profiles/{id}` | Update profile details |
| **DELETE** | `/api/profiles/{id}` | Delete profile |
| **POST** | `/profiles/{id}/photo` | Upload profile image |
| **GET** | `/profiles/{id}/photo` | Serve uploaded photo |
| **GET** | `/profiles/{id}/pdf` | Download ID card PDF |
| **GET** | `/profiles/{id}/qrcode` | Serve QR code PNG |
| **GET** | `/profiles/{id}/barcode` | Serve Barcode PNG |
| **POST** | `/batch/create` | Batch create multiple profiles |
| **GET** | `/batch/pdf` | Download ZIP containing all PDFs |
| **GET** | `/verify/{uuid}` | Public card verification portal |
