# Fitur & API Endpoint

## Register
**POST** /api/auth/register
- Job Seeker
- Company
```json
{
  "email": "...",
  "password": "..."
  "role": "JOB_SEEKER/COMPANY" 
}
```
---
## Login
**POST** /api/auth/register
- Job Seeker
- Company
- Human Resource
```json
{
  "identifier": "email/username",
  "password": "..."
}
```
---
## Login - Admin
**POST** /api/auth/login/admin
- Admin
```json
{
  "email": "admin@ippl.com",
  "password": "Admin1234",
  "otp": "..."
}
``` 
---
## Update Profile
**PUT** /api/user/update-profile
- Job Seeker
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
- Company
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
---
## Activity Log
**GET** /api/admin/logs
- Admin
---
## Generate HR Account
**POST** /api/company/generate-hr?companyEmail=...
- Company
---
