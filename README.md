# Smriti Setu (স্মৃতি সেতু)

**Multilingual Cognitive Games & Caregiver Dementia Decline Monitoring Platform for North East India**

Smriti Setu is a culturally attuned web platform crafted for individuals experiencing cognitive decline or dementia in North East India, and their dedicated caregivers. It converts the original Android app into a high-performance modern web application.

---

## Key Features

### 1. Culturally Attuned Cognitive Exercises
- **Regional Orientation:** Days of the week, local seasons, festivals (Bihu, Hornbill, Chapchar Kut, Losar), and North East landmarks (Kaziranga, Majuli, Loktak Lake, Brahmaputra).
- **Domain Training:** 
  - **Orientation:** Day & Date, Place & Time Recognition.
  - **Memory:** Memory Match (interactive pair flipping featuring cultural icons like Rhinos, Tea Leaves, Hornbill Feathers, and Orchids), Remember Objects.
  - **Attention:** Spot the Target Symbol, Sequential Numbers.
  - **Reasoning:** Cultural Patterns, Story Sequencing.
- **Adaptive Pacing & Zero-Stress Feedback:** Unhurried question timing, supportive encouragement, and adaptive difficulty scaling (Level 1 Gentle, Level 2 Moderate, Level 3 Engaging).

### 2. Comprehensive Multilingual Support (8 Regional Languages)
- **English**
- **অসমীয়া (Assamese)**
- **বাংলা (Bengali)**
- **हिन्दी (Hindi)**
- **মৈতৈলোন্ (Manipuri / Meitei)**
- **Ka Ktien Khasi (Khasi)**
- **Mizo ṭawng (Mizo)**
- **Nagamese (Nagaland Creole)**

### 3. Senior & Dementia Accessibility
- **High-Contrast Palette:** Dark slate contrast background with vivid amber/yellow accents for cataract/visual clarity.
- **Dynamic Text Scaling:** Standard (A), Large (A+), Extra-Large (A++).
- **Large Touch Targets:** 48px+ touch buttons designed for tablets and tremor mitigation.
- **Voice Read-Aloud (Text-to-Speech):** Integrated Web Speech API audio reading questions and feedback aloud in real time.

### 4. Patient & Caregiver Portals
- **Patient Portal:**
  - Daily routine checklist (Morning, Afternoon, Evening).
  - Recommended games of the day.
  - Interactive cognitive exercises.
  - Individual progress history and unique Patient Code linking.
- **Caregiver Dashboard:**
  - Longitudinal performance trend analytics (accuracy percentages, response times).
  - Decline detection indicator (improving, stable, noticeable variation) with clinically responsible non-diagnostic guidance.
  - Reminders & medication scheduler (Medications, Hydration, Doctor appointments).
  - Patient linking via unique code (`NER-xxxx`).

---

## Tech Stack
- **Framework:** React 19 + TypeScript
- **Bundler:** Vite
- **Styling:** Tailwind CSS + Lucide Icons
- **Speech Synthesis:** Web Speech API (TTS)
- **Persistence:** Local Storage persistence engine
- **Visuals & Charts:** Interactive SVG analytics and data trends

---

## Running Locally

```bash
# 1. Install dependencies
npm install

# 2. Start local development server
npm run dev

# 3. Build production bundle
npm run build
```

---

## Medical Disclaimer
*Smriti Setu is an assistive cognitive stimulation and routine care platform. It is **not** a clinical diagnostic tool or medical device. All indicators are intended for caregiver observation and routine organization.*
