# Fitur & API Endpoint

## AUTHENTICATION

### Register

**POST** /api/auth/registration

- Job Seeker
- Company

```json
{
  "email": "...",
  "password": "..."
  "role": "Job Seeker/Company"
}
```

### Login

**POST** /api/auth/session

- Job Seeker
- Company
- Human Resource

```json
{
  "username": "...",
  "password": "..."
}
```

### Login - Admin

**POST** /api/auth/admin-session

- Admin

```json
{
  "email": "admin@difability.com",
  "password": "Admin123",
  "otp": "..."
}
```

### Logout

**DELETE** /api/auth/session

- All

---

## USER

### Get All Users

**GET** /api/users

### Get User by Id

**GET** /api/users/{userId}

### Delete User by Id

**DELETE** /api/users/{userId}

### Update Profile JobSeeker

**PATCH** /api/jobseekers/me/profile

```json
{
  "name": "...",
  "about": "...",
  "address": "...",
  "disabilityType": "...",
  "skills": ["...", "...", "..."],
  "educationLevel": "...",
  "academicYear": "...",
  "jobType": "...",
  "ppImgPath": "/...",
  "cvFilePath": "/...",
  "certifFilePaths": ["/...", "/..."]
}
```

### Update Profile Company

**PATCH** /api/companies/me/profile

```json
{
  "name": "...",
  "description": "...",
  "address": "...",
  "industryType": "...",
  "websiteUrl": "...",
  "linkedinUrl": "...",
  "youtubeUrl": "...",
  "instagramUrl": "...",
  "twitterUrl": "...",
  "logoImgPath": "/..."
}
```

### Update Profile Human Resource

**PATCH** /api/humanresources/me/profile

```json
{
  "name": "...",
  "description": "...",
  "address": "...",
  "industryType": "...",
  "websiteUrl": "...",
  "logoImgPath": "/..."
}
```

---

## APPLICATION

### Get Application by Id

**GET** /api/applications/{applicationId}

### Create Application

**POST** /api/jobs/{jobId}/applications

```json
{
  "coverLetter": ""
}
```

### Review Application

**PATCH** /api/applications/{applicationId}/

```json
{
  "status": "Accepted/Declined",
  "hrNotes": "..."
}
```

### Delete Application

**DELETE** /api/applications/{applicationId}

---

## Company

### Generate HR Account

**POST** /api/companies/me/humanresources

---

## Logs

### Get All Logs

**GET** /api/logs

### Get Logs by Username

**GET** /api/logs/users/{username}

### Get Logs by Role

**GET** /api/logs/roles/{role}

### Get Logs by Action

**GET** /api/logs/actions/{action}

### Create Job

**POST** /api/jobs

```json
{
  "title": "...",
  "description": "...",
  "salary": 1000,
  "minimumEducation": "...",
  "minimumYearsExperience": 1,
  "compatibleDisabilities": ["...", "..."],
  "registrationDeadline": "2025-12-31T23:59:59",
  "publicationStatus": "Open"
}
```

## JOB

### Get All Jobs

**GET** /api/jobs

### Get Job by Id

**GET** /api/jobs/{jobId}

### Create Job

**POST** /api/jobs

```json
{
  "title": "",
  "jobDescription": "",
  "salary": ...,
  "minimumEducation": "",
  "minimumYearsExperience": 2,
  "compatibleDisabilities": ["", ""],
  "registrationDeadline": "YYYY-MM-DDTHH:MM:SS"
}
```

---

### Delete Job by Id

**DELETE** /api/jobs/{jobId}

## FILE

### Upload Image

**POST** /api/files/upload/image  
type: "file" - png/jpg/jpeg

### Upload Document

**POST** /api/files/upload/document  
type: "file" - pdf

### View File

**GET** /api/files/view

---

## ENUM

### Application Statuses

Under Review | Accepted | Declined

**GET** /api/enums/application-statuses

### Disability Types

Visual | Hearing | Mobility | Cognitive

**GET** /api/enums/disability-types

### Education Levels

High School | College Student | Bachelor | Master | Doctorate

**GET** /api/enums/education-levels

### Industry Types

Technology | Healthcare | Education | Finance | E-Commerce | Media | Others

**GET** /api/enums/industry-types

### Job Types

Full Time | Freelance | Contract | Remote | Internship

**GET** /api/enums/job-types

### Publication Statuses

Open | Closed

**GET** /api/enums/publication-statuses
