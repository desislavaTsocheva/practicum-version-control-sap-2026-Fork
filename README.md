[//]: # (# 📘 Practicum Version Control — SAP 2026)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 👥 Our Team)

[//]: # ()
[//]: # (- [**Marieta Stoycheva**]&#40;https://github.com/mstoycheva&#41;)

[//]: # (- [**Desislava Tsocheva**]&#40;https://github.com/desislavaTsocheva&#41;)

[//]: # (- [**Milena Boyadzhieva**]&#40;https://github.com/MilenaB12&#41;)

[//]: # (- [**Bozhidara Malkocheva**]&#40;https://github.com/BMalkocheva&#41;)

[//]: # (- [**Denitsa Ilieva**]&#40;https://github.com/DenitsaIlieva3&#41;)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 📌 Overview)

[//]: # ()
[//]: # (This repository is part of the **Version Control Practicum** for SAP 2026.  )

[//]: # (Its purpose is to introduce students to the fundamental concepts of **Git** and **GitHub**, focusing on practical skills such as repository management, branching strategies, collaboration workflows, and maintaining clean project history.)

[//]: # ()
[//]: # (The project includes example files and exercises designed to help students gain confidence when working with version control systems in real development environments.)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🧭 Objectives)

[//]: # ()
[//]: # (The practicum aims to develop the following competencies:)

[//]: # ()
[//]: # (- Creating and managing Git repositories)

[//]: # (- Working with branches and understanding branching models)

[//]: # (- Making commits following best practices)

[//]: # (- Merging changes and resolving conflicts)

[//]: # (- Using GitHub for collaboration &#40;push, pull, pull requests&#41;)

[//]: # (- Maintaining clear and structured commit history)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## ⚙️ Technologies Used)

[//]: # ()
[//]: # (During the development of this project, our team worked with several tools and technologies that support professional version control workflows and collaborative development. The main technologies we used include:)

[//]: # ()
[//]: # (- **Git** – for version control, branching, merging, and maintaining a structured commit history)

[//]: # (- **GitHub** – for remote repository management, Pull Requests, code reviews, and team collaboration)

[//]: # (- **Markdown** – for creating clear and well‑structured project documentation)

[//]: # (- **Command Line Tools &#40;CLI&#41;** – for executing Git commands and managing the repository efficiently)

[//]: # (- **Integrated Development Environments &#40;IDEs&#41;** – such as IntelliJ IDEA and VS Code, used to edit and organize project files)

[//]: # (- **GitHub Desktop / Git Bash** – optional tools used by some team members to streamline workflow)

[//]: # (- **Spring Boot** – used as part of our environment setup and project structure exploration)

[//]: # (- **Thymeleaf** – used for demonstrating template rendering and project organization within a Spring Boot setup)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🛠️ Project Functionality)

[//]: # ()
[//]: # (Our team developed a project that demonstrates the practical use of Git and GitHub in a collaborative environment.  )

[//]: # (We focused on applying structured workflows and maintaining a clean and organized repository.)

[//]: # ()
[//]: # (Key functionalities include:)

[//]: # ()
[//]: # (- Team collaboration through a shared GitHub repository)

[//]: # (- Clear tracking and documentation of all changes)

[//]: # (- Reliable versioning to prevent data loss)

[//]: # (- Maintaining an organized and readable commit history)

[//]: # (- Applying professional branching, merging, and commit practices)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 🤝 Team Contributions)

[//]: # ()
[//]: # (Throughout the development of this project, each team member contributed to the structure, clarity, and overall quality of the repository.  )

[//]: # (Our collaborative work ensured that the project is well‑organized, consistent, and aligned with professional version control practices.)

[//]: # ()
[//]: # (As a team, we worked together to:)

[//]: # ()
[//]: # (- refine the project structure)

[//]: # (- maintain clear and readable documentation)

[//]: # (- ensure consistent commit history)

[//]: # (- improve the workflow and organization of the repository)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## ▶️ How to Run)

[//]: # ()
[//]: # (To access and work with the project, follow these steps:)

[//]: # ()
[//]: # (1. **Clone the repository**)

[//]: # (   ```bash)

[//]: # (   git clone https://github.com/mstoycheva/practicum-version-control-sap-2026.git)

[//]: # (   ```)

[//]: # ()
[//]: # (---)

[//]: # ()
[//]: # (## 📜 License)

[//]: # ()
[//]: # (This repository is intended solely for educational use within the SAP Practicum 2026 program.)

# 📘 Practicum Version Control — SAP 2026

---

## 👥 Our Team & Roles

| Member | Role | GitHub |
| :--- | :--- | :--- |
| **Marieta Stoycheva** | Backend & Frontend Developer | [@mstoycheva](https://github.com/mstoycheva) |
| **Desislava Tsocheva** | Backend Developer | [@desislavaTsocheva](https://github.com/desislavaTsocheva) |
| **Milena Boyadzhieva** | QA Engineer | [@MilenaB12](https://github.com/MilenaB12) |
| **Bozhidara Malkocheva** | Technical Writer | [@BMalkocheva](https://github.com/BMalkocheva) |
| **Denitsa Ilieva** | Technical Writer | [@DenitsaIlieva3](https://github.com/DenitsaIlieva3) |

---

## 📌 Overview
This repository is part of the **Version Control Practicum** for SAP 2026. The project focuses on mastering **Git** and **GitHub** workflows while implementing a robust application based on a **Microservices Architecture**.

---

## ⚙️ Technologies Used

* **Backend:** Java & Spring Boot
* **Frontend:** Thymeleaf, JavaScript & CSS3, HTML5
* **Database:** MySQL (Aiven Cloud)
* **Infrastructure:** Spring Cloud (Eureka, API Gateway)
* **Tools:** Git, GitHub, Maven, CLI

---

## 🏗️ System Architecture
The project is built using a microservices pattern where individual services maintain their own databases and communicate through a centralized gateway to ensure modularity and scalability:

* **`auth-microservice`**: Handles user authentication and security protocols.
* **`project-microservice`**: Manages core project data and business logic.
* **`document-microservice`**: Dedicated to document management and processing.
* **`eureka-microservice`**: Acts as the Service Discovery server for the ecosystem.
* **`api-gateway`**: Routes client requests to the appropriate microservices.

---

## 🛠️ Project Functionality
* Team collaboration through professional branching and merging strategies.
* Secure user authentication and session management.
* Comprehensive project and document lifecycle management.
* Dynamic service discovery and centralized API routing.
* Cloud-integrated database management for persistent storage.

---

## ▶️ How to Run

### 📋 Prerequisites
Ensure the following are installed on your system:
* **Java SDK** (Version 17 or 21)
* **Maven**
* **Git**

### 🚀 Installation & Launch
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/mstoycheva/practicum-version-control-sap-2026.git](https://github.com/mstoycheva/practicum-version-control-sap-2026.git)

2. **Navigate to the project folder:**
   ```bash
   cd practicum-version-control-sap-2026

3. **Install dependencies:**
   ```bash 
   mvn install

4. **Access the application:**
   ``` bash 
   http://localhost:8080/auth-microservice/