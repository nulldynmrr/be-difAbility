# Features & API Endpoints

## Auth

**Base URL:** `/api/auth`

### Register

**POST** `/registration`  
Access: Public

```json
{
  "email": "user@example.com",
  "password": "password123",
  "role": "Job Seeker/Company"
}
```

### Login

**POST** `/session`  
Access: Public

```json
{
  "username": "user@example.com",
  "password": "password123"
}
```

### Login - Admin

**POST** `/admin-session`  
Access: Public

```json
{
  "email": "admin@difability.com",
  "password": "Admin123",
  "otp": "123456"
}
```

### Logout

**DELETE** `/session`  
Access: Authenticated Users

---

## User

**Base URL:** `/api/users`

### Get All Users

**GET** `/`  
Access: ADMIN

### Delete User by Id

**DELETE** `/{userId}`  
Access: ADMIN

---

## Company

**Base URL:** `/api/companies`

### Get Company Profile (Public/Shared)

**GET** `/{companyId}/profile`  
Access: Authenticated Users

### Get My Profile

**GET** `/me/profile`  
Access: COMPANY

### Update My Profile

**PATCH** `/me/profile`  
Access: COMPANY

```json
{
  "name": "Company Name",
  "description": "Description...",
  "address": "Address...",
  "industryType": "Technology",
  "websiteUrl": "https://...",
  "linkedinUrl": "https://...",
  "youtubeUrl": "https://...",
  "instagramUrl": "https://...",
  "twitterUrl": "https://...",
  "logoImgPath": "/uploads/images/...",
  "agreeToTerms": "true"
}
```

### Generate HR Account

**POST** `/me/humanresources`  
Access: COMPANY

### Get Human Resources List

**GET** `/me/humanresources`  
Access: COMPANY

---

## Job Seeker

**Base URL:** `/api/jobseekers`

### Get Job Seeker Profile (Public/Shared)

**GET** `/{jobseekerId}/profile`  
Access: Authenticated Users

### Get My Profile

**GET** `/me/profile`  
Access: JOB_SEEKER

### Update My Profile

**PATCH** `/me/profile`  
Access: JOB_SEEKER

```json
{
  "name": "Full Name",
  "about": "Summary...",
  "address": "Address...",
  "disabilityType": "Visual",
  "skills": ["Java", "Spring", "SQL"],
  "educationLevel": "Bachelor",
  "academicYear": "2024",
  "jobType": "Full Time",
  "ppImgPath": "uploads/images/...",
  "cvFilePath": "uploads/documents/...",
  "certifFilePaths": ["/uploads/documents/...1", "/uploads/documents/...2"]
}
```

---

## Human Resource

**Base URL:** `/api/humanresources`

### Get HR Profile (Public/Shared)

**GET** `/{hrId}/profile`  
Access: Authenticated Users

### Get My Profile

**GET** `/me/profile`  
Access: COMPANY

### Update My Profile

**PATCH** `/me/profile`  
Access: HUMAN_RESOURCE

```json
{
  "fullName": "Full Name",
  "contact": "081124124124",
  "ppImagePath": "uploads/images/..."
}
```

---

## Job

**Base URL:** `/api/jobs`

### Get All Jobs

**GET** `/`  
Access: Authenticated Users

### Get Job by Id

**GET** `/{jobId}`  
Access: Authenticated Users

### Create Job

**POST** `/`  
Access: HUMAN_RESOURCE, COMPANY

```json
{
  "title": "Software Engineer",
  "description": "Job details...",
  "salary": 5000000,
  "minimumEducation": "Bachelor",
  "minimumYearsExperience": 2,
  "compatibleDisabilities": ["Hearing", "Mobility"],
  "registrationDeadline": "2025-12-31T23:59:59",
  "publicationStatus": "Open"
}
```

### Delete Job

**DELETE** `/{jobId}`  
Access: HUMAN_RESOURCE, COMPANY

---

## Application

**Base URL:** `/api`

### Get Application

**GET** `/jobs/{jobId}/applications/{applicationId}`  
Access: HUMAN_RESOURCE

### Create Application

**POST** `/jobs/{jobId}/applications`  
Access: JOB_SEEKER

```json
{
  "coverLetter": "I am interested in this position..."
}
```

### Review Application

**PATCH** `/jobs/{jobId}/applications/{applicationId}`  
Access: HUMAN_RESOURCE

```json
{
  "status": "Accepted",
  "hrNotes": "Candidate meets requirements."
}
```

### Delete Application

**DELETE** `/applications/{applicationId}`  
Access: JOB_SEEKER

---

## Log

**Base URL:** `/api/logs`

### Get All Logs

**GET** `/`  
Access: ADMIN

### Get Logs by Role

**GET** `/roles/{role}`  
Access: ADMIN

---

## File

**Base URL:** `/api/files`

### Upload Image

**POST** `/upload/image`  
Type: multipart/form-data (png/jpg/jpeg)  
Access: Authenticated Users

### Upload Document

**POST** `/upload/document`  
Type: multipart/form-data (pdf)  
Access: Authenticated Users

### View File

**GET** `/view`  
Access: Authenticated Users

---

## Enum

**Base URL:** `/api/enums`

### Application Statuses

**GET** `/application-statuses`  
Values: Under Review | Accepted | Declined

### Disability Types

**GET** `/disability-types`  
Values: Visual | Hearing | Mobility | Cognitive

### Education Levels

**GET** `/education-levels`  
Values: High School | College Student | Bachelor | Master | Doctorate

### Industry Types

**GET** `/industry-types`  
Values: Technology | Healthcare | Education | Finance | E-Commerce | Media | Others

### Job Types

**GET** `/job-types`  
Values: Full Time | Freelance | Contract | Remote | Internship

### Publication Statuses

**GET** `/publication-statuses`  
Values: Open | Closed
