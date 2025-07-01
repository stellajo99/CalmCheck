# CalmCheck 🧠  
*A Calm, Smart Health Companion – Personalized AI Support for Everyday Symptoms*

## 📱 Overview

**CalmCheck** is a mobile application designed to reduce health-related anxiety by combining natural language AI interactions with personalized health guidance. It allows users to describe physical or emotional symptoms in their own words and receive calm, medically responsible suggestions. The app also displays relevant health information and evolves over time by learning from the user's interaction history.

---

## 🗂 Project Structure

```
CalmCheck/
├── backend/                 # Flask backend server
│   ├── app.py              # API routes for chat, feed, and history
│   ├── db.py               # SQLite-based local database operations
│   ├── feed.py             # GNews API integration for health articles
│   ├── llama_client.py     # OpenRouter API integration with LLaMA 4
│   ├── personalize.py      # Prompt generation based on user history
│   └── utils.py            # NLP and severity detection utilities
├── frontend/               # Android app (Java with Android Studio)
│   └── [Java code, XML layouts, assets, etc.]
```

---

## ⚙️ Backend Setup

### 1. 🐍 Environment Setup

```bash
cd CalmCheck/backend
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install flask requests python-dotenv spacy
python -m spacy download en_core_web_sm
```

---

### 2. 🔑 Environment Variables

Create a `.env` file in `backend/` with your OpenRouter API key and GNews token:

```
OPENROUTER_API_KEY=your-openrouter-key
GNEWS_API_KEY=your-gnews-api-key
```

---

### 3. 🚀 Run the Backend

```bash
python app.py
```

This launches the Flask server on [http://localhost:5000](http://localhost:5000) with the following endpoints:

| Method | Endpoint       | Description                           |
|--------|----------------|---------------------------------------|
| POST   | `/chat`        | Submit symptom input + get AI reply   |
| GET    | `/feed`        | Get health-related articles           |
| GET    | `/history`     | Retrieve previous chats per user      |

---

## 💡 Core Features

### 🔬 AI-Powered Symptom Support
- Accepts user input in natural language
- Generates calming responses via **LLaMA 4** using OpenRouter API
- Only triggers alerts like **“See a Doctor!!”** for critical symptoms (contextually aware)

### 📰 Trusted Article Feed
- Uses **GNews API** to fetch health-related articles
- Displays image, title, and summary in app’s news section

### 🧠 Personalization Engine
- Stores prior symptoms per user using **SQLite**
- Contextually alters tone & suggestions based on user history
- Filters redundant alerts based on smart keyword tracking

---

## 📦 Local DB

- All user data and chat logs are stored in a local **SQLite** file (`calmcheck.db`)
- Table structure: `chat_history(user_id, input, response, summary, severity, timestamp)`

---

## 🧠 LLaMA 2 Integration (OpenRouter)

- Prompt built using `personalize.py` with:
  - recent chat history
  - extracted keywords
  - AI tone control (calming, non-alarming)
- Uses POST requests to OpenRouter with `meta-llama/llama-4-maverick:free` model

---

## 📱 Android App Overview (Frontend)

- Built using **Java (Android Studio)**
- Local DB caching via SQLite
- Core screens:
  - Login / Sign-up (local storage-based)
  - AI Chat Interface (bubble layout)
  - News Feed (scrollable card list with GNews data)

---

## 🔮 Future Improvements

- Cloud sync using Firebase or Firestore
- On-device LLaMA inference for privacy
- Multilingual chatbot support
- Push notifications for chronic symptoms

---

## 🧑‍💻 Author

**Stella Jo**  
SIT708 Mobile Application Development  
GitHub: [github.com/stellajo99](https://github.com/stellajo99)