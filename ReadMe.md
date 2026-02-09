# ResearchOps Project

A role-based case management system designed to manage research/legal cases with clear separation of responsibilities between **Admin**, **Case Manager**, and **Researcher**.

The system focuses on controlled access, structured workflows, and efficient case handling, including **auto-generated and reusable Case IDs** during case creation.

---

## 👥 User Roles & Capabilities

The application supports three primary user roles:

* **Admin**
* **Case Manager**
* **Researcher**

Each role has specific permissions and actions, described below.

---

## 🛡️ Admin Actions

Admins have full visibility and high-level control over users and cases.

### User Management

* Create **Case Manager** profiles
* Create **Researcher** profiles
* View all registered users
* View **active** and **inactive** user profiles
* Enable or disable user access

### Case Oversight

* View the complete list of registered cases
* View detailed information for any case
* Edit or update case details when required

---

## 📁 Case Manager Actions

Case Managers are responsible for creating, managing, and assigning cases.

### Authentication

* Login to the system using registered credentials

### Case Management

* View a list of all cases created or assigned to them
* Create a **new case**
* Edit existing case details

### Case Assignment

* Assign cases to **Researchers**
* Select researchers from existing, active researcher profiles

### Case ID Behavior

* Case ID is **auto-generated** when initiating case creation
* Case ID is **temporary and reusable** until the final case form is submitted
* If the case creation is abandoned, the Case ID becomes available again

---

## 🔬 Researcher Actions

Researchers focus on working only on cases assigned to them.

### Authentication

* Login to the system using registered credentials

### Case Access

* View a list of cases assigned to them
* View detailed information of each assigned case

### Restrictions

* Researchers **cannot create or edit cases**
* Access is limited strictly to assigned cases only

---

## 🔑 Key System Features

* Role-based access control (RBAC)
* Secure login for all users
* Auto-generated, reusable Case IDs before final submission
* Clear separation of responsibilities between roles
* Support for active/inactive user states

---

## 🚀 Typical Workflow

1. **Admin** creates Case Manager and Researcher profiles
2. **Case Manager** logs in and creates a new case
3. System auto-generates a temporary Case ID
4. Case Manager assigns the case to one or more Researchers
5. Case details are finalized and saved
6. **Researchers** log in and work on assigned cases

---

## 📌 Notes

* Case IDs are finalized **only after successful case submission**
* Incomplete or abandoned case creation flows do not permanently consume Case IDs
* All actions are governed by role-based permissions

---

This project is designed to be extensible, secure, and aligned with real-world case management workflows.
