# Fitur & API Endpoint

## Register
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
---
## Login
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
---
## Login - Admin
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
## Update Profile
- Job Seeker  
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
- Company  
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
---
## Activity Log
**GET** /api/admin/logs  
Header: Bearer Token  
- Admin
---
## Generate HR Account
**POST** /api/company/generate-hr  
Header: Bearer Token  
- Company
---
