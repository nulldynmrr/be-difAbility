# Fitur & API Endpoint

## AUTHENTICATION

### Register

**POST** /api/auth/registrations  
Header: None

- Job Seeker
- Company

```json
{
  "email": "...",
  "password": "..."
  "role": "JOB_SEEKER/COMPANY"
}
```

### Login

**POST** /api/auth/sessions  
Header: None

- Job Seeker
- Company
- Human Resource

```json
{
  "identifier": "email/username",
  "password": "..."
}
```

### Login - Admin

**POST** /api/auth/admin-sessions  
Header: None

- Admin

```json
{
  "email": "admin@ippl.com",
  "password": "Admin1234",
  "otp": "..."
}
```

---

## JOBSEEKER

### Update Profile

**PATCH** /api/jobseeker/profile  
Header: Bearer Token

```json
{
  "name": "...",
  "about": "...",
  "address": "...",
  "disabilityType": "Visual/Hearing/Mobility/Cognitive",
  "skills": ["...", "...", "..."],
  "educationLevel": "...",
  "ppImgPath": "/...",
  "cvFilePath": "/...",
  "certifFilePaths": ["/...", "/..."]
}
```

### Create Application

**POST** /api/jobs/{jobId}/applications  
Header: Bearer Token

---

## COMPANY

### Update Profile

**PATCH** /api/company/profile  
Header: Bearer Token

```json
{
  "name": "...",
  "description": "...",
  "address": "...",
  "industryType": "Technology/Healthcare/Education/Finance/E-Commerce/Media/Others",
  "websiteUrl": "...",
  "logoImgPath": "/..."
}
```

### Generate HR Account

**POST** /api/company/hr-accounts
Header: Bearer Token

---

## ADMIN

### Activity Log

**GET** /api/activity-logs
Header: Bearer Token

---

## HUMAN RESOURCE

### Create Job

**POST** /api/jobs  
Header: Bearer Token

```json
{
  "title": "...",
  "description": "...",
  "salary": 1000,
  "minimumEducation": "High School/College Student/Bachelor/Master/Doctorate",
  "minimumYearsExperience": 1,
  "compatibleDisabilities": ["Visual/Hearing/Mobility/Cognitive", "..."],
  "registrationDeadline": "2025-12-31T23:59:59",
  "publicationStatus": "Open"
}
```

### Review Job

**PATCH** /api/applications/{applicationId}/  
Header: Bearer Token

```json
{
  "status": "Accepted/Declined",
  "hrNotes": "..."
}
```
