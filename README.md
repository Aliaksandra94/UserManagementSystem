# UserManagementSystem
Test task - User Management System.
A WEB application that represents the user management interface (with i18n).

**Before running the test of this web_app, please, follow the below instruction: **

Create DB and fill it on the    application.properties - file. 

USER Role:
Available actions: Authorization; View the list of users; View any user's details; Logout.
Login/pass: USER/user1 (status active); USERIO/user2.

ADMIN Role: 
Available actions:Authorization; View the list of users; View the details of any user; Creating a new user; Edit an existing user; Lock / Unlock the user; Logout.
Login/pass: ADMIN/admin1.

Page URL Description (based on the role):
1) Login -/login           - Login to the system Login to the system; If the user's state is INACTIVE, then login is not possible; If the username/password is not correct, the login is not
possible. 
2) List  -/user            - List of users - 1. Filter the list by Username; 2. Filtering the list by Role; 3. Pagination.
3) View  -/user/{id}      - View user details
4) New   -/user/new        - Creating a new user (corresponding message about validation errors are displayed on the UI)
5) Edit  -/user/{id}/edit  - Edit User (corresponding message about validation errors are displayed on the UI)

Technology Stack:
1. Java 8+
2. Spring (Spring Boot, Spring MVC, Spring Data, Spring Security)
3. PostgreSQL
4. Html Template Engine - Thymeleaf
5. Gradle
