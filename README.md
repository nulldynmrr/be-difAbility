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

**GET** /api/company/profile  
Header: Bearer Token

**POST** /api/company/profile  
Header: Bearer Token

**PATCH** /api/company/profile  
Header: Bearer Token

```json
{
  "name": "...",
  "description": "...",
  "address": "...",
  "industryType": "Technology/Healthcare/Education/Finance/E-Commerce/Media/Others",
  "websiteUrl": "...",
  "logoImgPath": "/...",
  "linkedinUrl": "",
  "youtubeUrl": "",
  "instagramUrl": "",
   "twitterUrl": "",
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


# CHAT API (REST & WebSocket)

Dokumentasi ini menjelaskan spesifikasi **Chat** antara **Jobseeker** dan **Company/HR** berdasarkan **Job**.

---

## 🔐 Authentication

Semua endpoint **REST API** dan **WebSocket** menggunakan:

```
Authorization: Bearer <TOKEN>
```

---

## 📌 REST API

### 1️⃣ Create / Get Conversation

Membuat **conversation baru** atau **mengambil conversation yang sudah ada** berdasarkan `jobId` dan `jobSeekerId`.

> ⚠️ Jika conversation sudah ada → **tidak membuat baru**

#### Endpoint

```
POST /api/conversations
```

#### Request Body

```json
{
  "jobId": 10,
  "jobSeekerId": 5,
  "initialMessage": "Halo, saya tertarik dengan lowongan ini"
}
```

| Field          | Type   | Required | Description           |
| -------------- | ------ | -------- | --------------------- |
| jobId          | number | ✅        | ID lowongan pekerjaan |
| jobSeekerId    | number | ✅        | ID jobseeker          |
| initialMessage | string | ❌        | Pesan awal (opsional) |

#### Response

```json
{
  "id": 3,
  "jobId": 10,
  "jobTitle": "Backend Developer",
  "companyId": 2,
  "companyName": "PT Contoh",
  "jobSeekerId": 5,
  "jobSeekerName": "andi",
  "status": "ACTIVE",
  "startedAt": "2025-12-15T10:00:00",
  "lastMessageAt": "2025-12-15T10:01:00",
  "lastMessageContent": "Halo, saya tertarik...",
  "unreadCount": 0,
  "recentMessages": []
}
```

---

### 2️⃣ Get User Conversations

Mengambil **seluruh conversation** milik user (Jobseeker / Company / HR).

#### Endpoint

```
GET /api/conversations
```

#### Response

```json
[
  {
    "id": 3,
    "jobId": 10,
    "jobTitle": "Backend Developer",
    "companyId": 2,
    "companyName": "PT Contoh",
    "jobSeekerId": 5,
    "jobSeekerName": "andi",
    "status": "ACTIVE",
    "startedAt": "2025-12-15T10:00:00",
    "lastMessageAt": "2025-12-15T10:01:00",
    "lastMessageContent": "Halo...",
    "unreadCount": 2,
    "recentMessages": null
  }
]
```

---

### 3️⃣ Get Messages in Conversation

Mengambil isi chat dalam conversation tertentu **dan otomatis menandai pesan sebagai telah dibaca**.

#### Endpoint

```
GET /api/conversations/{conversationId}/messages
```

#### Response

```json
[
  {
    "id": 20,
    "conversationId": 3,
    "senderId": 5,
    "senderName": "andi",
    "senderRole": "JOBSEEKER",
    "messageContent": "Halo...",
    "createdAt": "2025-12-15T10:01:00",
    "isRead": true
  }
]
```

---

## ⚡ WebSocket (Realtime Chat)

### 1️⃣ Send Message

Mengirim pesan chat secara realtime.

#### WS Destination

```
/app/chat.send
```

#### Payload

```json
{
  "conversationId": 3,
  "messageContent": "Baik, terima kasih"
}
```

#### Broadcast To

```
/topic/conversation/{conversationId}
```

---

### 2️⃣ Typing Indicator

Mengirim notifikasi bahwa user sedang mengetik.

> ⚠️ Tidak disimpan ke database

#### WS Destination

```
/app/chat.typing
```

#### Payload

```json
{
  "conversationId": 3,
  "typing": true
}
```

#### Broadcast To

```
/topic/conversation/{conversationId}/typing
```

---
