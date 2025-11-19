# Fitur & API Endpoint

## AUTHENTICATION

### Register

**POST** /api/auth/register  
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

**POST** /api/auth/register  
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

**POST** /api/auth/login/admin  
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

## Update Profile

**PUT** /api/user/jobseeker-profile  
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

**POST** /api/application/{jobId}  
Header: Bearer Token

---

## COMPANY

## Update Profile

**PUT** /api/user/company-profile  
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

## Generate HR Account

**POST** /api/company/generate-hr  
Header: Bearer Token

---

## ADMIN

### Activity Log

**GET** /api/admin/logs  
Header: Bearer Token

---

## HUMAN RESOURCE

### Create Job

**POST** /api/job/create  
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

---
