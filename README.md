# API ENDPOINT

### Register - POST
/api/auth/register

- Register Company
- Register Job Seeker

contoh: 
{
  "email": "company1@ippl.com",
  "password": "Password123",
  "role": "COMPANY"
}

### Login - POST
/api/auth/login

- Login Company 
- Login Job Seeker
- Login Human Resource

contoh: 
Company/Job Seeker - Email
{
  "identifier": "company1@ippl.com",
  "password": "Password123"
}

Human Resource - Username
{
  "identifier": "company1_hr_738",
  "password": "DV17pv0jmeyn"
}

### Login Admin - POST
/api/auth/login/admin

- Login Admin

contoh:
{
  "email": "admin@ippl.com",
  "password": "Admin1234!",
  "otp": "605196"
}

### Create HR Account - POST
/api/company/generate-hr?companyEmail=[email]

- Company

contoh:
http://localhost:8080/api/company/generate-hr?companyEmail=company1@ippl.com

response:
{
  "username": "test3_hr_738",
  "password": "DV17pv0jmeyn",
  "role": "HUMAN_RESOURCE"
}

