# 📦 Projet Fullstack 2025 – Mastering

Application complète Angular Web + Ionic Mobile + Spring Boot Backend.

---

## 🧱 Architecture du Projet

Ce projet est divisé en **trois dépôts GitHub distincts** :

| Partie        | Techno         | Repository GitHub                                                                 |
|---------------|----------------|------------------------------------------------------------------------------------|
| 🖥️ Web        | Angular 17+    | 🔗 [MasteringWeb](https://github.com/fenMel/MasteringWeb)                         |
| 📱 Mobile     | Ionic 7 (Angular) | 🔗 [MasteringMobile](https://github.com/fenMel/MasteringMobile)                 |
| 🔙 Backend    | Spring Boot 3.2.2 (Java 21) | 🔗 [Mastering2025](https://github.com/fenMel/Mastering2025)     |

Chaque client (web et mobile) consomme l'**API REST sécurisée** fournie par le backend.

---

## 🔙 Backend – Spring Boot

📁 Repo : [`Mastering2025`](https://github.com/fenMel/Mastering2025)

### ✅ Stack

- Java 21
- Spring Boot 3.2.2
- MySQL / JPA
- Sécurité JWT
- SpringDoc OpenAPI (Swagger)
- Envoi d’e-mails (JavaMail)
- Génération PDF (iText + Flying Saucer)

### ▶️ Lancement

```bash
cd backend/
# Créer la base de données :
# CREATE DATABASE mastering CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
mvn clean install
mvn spring-boot:run


Documenation de :
FENZI MELISSA : https://drive.google.com/drive/folders/1sAXoGutal9ts1OVyZTNTqO3l0gVzXmc5?usp=drive_link
