# 🏭 Smart Warehouse Grid Locator

> **BTech Semester OOP Project** | Core Java Backend + HTML/CSS/JS Frontend

---

## 📋 Project Overview

A full-stack **Smart Warehouse Grid Locator** system that allows users to store, locate, update, and manage warehouse items inside a **5×5 2D grid**. The warehouse is represented using a `GridItem[][]` array where each cell can hold an item object or remain empty.

---

## 🗂️ Project Structure

```
SmartWarehouse/
├── backend/
│   ├── GridItem.java        ← Item model (Encapsulation, OOP)
│   ├── Warehouse.java       ← Core grid logic (2D Arrays, CRUD)
│   └── Main.java            ← Entry point, tests all operations
│
├── frontend/
│   ├── index.html           ← Dashboard UI
│   ├── style.css            ← Glassmorphism design, dark/light mode
│   └── app.js               ← JavaScript OOP (mirrors Java classes)
│
└── README.md
```

---

## 🔧 Tech Stack

| Layer    | Technology                  |
|----------|-----------------------------|
| Backend  | Core Java (No Spring Boot)  |
| Frontend | HTML5, CSS3, JavaScript     |
| Charts   | Chart.js (CDN)              |
| Design   | Glassmorphism + CSS Variables |

---

## 🧠 OOP Concepts Implemented

| Concept            | Where Used                                      |
|--------------------|-------------------------------------------------|
| **Classes**        | `GridItem`, `Warehouse`, `Main`                 |
| **Encapsulation**  | Private fields + public getters/setters         |
| **Data Hiding**    | All `GridItem` fields are `private`             |
| **Constructors**   | Default + Parameterized constructors            |
| **Method Overloading** | `addItem(GridItem, row, col)` and `addItem(String, String, int, row, col)` |
| **Objects**        | `GridItem` objects stored in `GridItem[][]`     |
| **2D Arrays**      | `GridItem[][] grid` – core data structure       |

---

## 🚀 How to Run

### Backend (Java)

```bash
# Navigate to backend folder
cd backend

# Compile all Java files
javac *.java

# Run the main class
java Main
```

**Expected Output:**
```
╔═══════════════════════════════════════════╗
║    SMART WAREHOUSE GRID LOCATOR v1.0      ║
║       Backend – Core Java (OOP)           ║
╚═══════════════════════════════════════════╝

► Initialising 5×5 warehouse grid...

[SUCCESS] Item 'I101' added at (0, 0).
[SUCCESS] Item 'I102' added at (1, 2).
...

Searching for Item ID: I102
[FOUND] Item ID: I102  →  Row: 1, Column: 2 | Phone | Qty: 25

Searching for Item ID: I999
[NOT FOUND] No item with ID 'I999' exists in the warehouse.
```

### Frontend (Browser)

```bash
# Simply open the file in any modern browser
cd frontend
start index.html     # Windows
open index.html      # macOS
```

No server required — runs entirely in the browser.

---

## ⚙️ Features

### Core CRUD Operations
| Operation | Method | Description |
|-----------|--------|-------------|
| Add Item  | `addItem()` | Place item at specific (row, col) |
| Search ID | `searchItemById()` | Find by unique ID using nested loops |
| Search Name | `searchItemByName()` | Find by keyword (partial match) |
| Update    | `updateQuantity()` | Modify stock count |
| Remove    | `removeItem()` | Delete item, mark slot empty |
| Display   | `displayGrid()` | Visual 2D grid in terminal / browser |

### Additional Features
- 📊 **Utilisation Chart** – doughnut chart (Chart.js)
- 📄 **Export Report** – downloads `.txt` warehouse report
- 🕐 **Recently Added** – tracks last 10 items
- 📋 **Search History** – logs last 20 searches
- 🌙 **Dark / Light Mode** – toggle with one click
- ✅ **Real-time validation** – duplicate IDs, invalid coords, negative qty
- 📱 **Mobile-friendly** – responsive glassmorphism UI

---

## 🗺️ Flowchart

```
        START
          │
          ▼
  Initialize 5×5 Grid
          │
          ▼
   Display Dashboard
          │
          ▼
  ┌───────────────────┐
  │  User Operation?  │
  └───────────────────┘
     │       │       │       │
     ▼       ▼       ▼       ▼
   ADD    SEARCH  UPDATE  REMOVE
     │       │       │       │
     ▼       ▼       ▼       ▼
  Validate  Traverse  Validate  Find by
  Input     2D Array  Qty ≥ 0   ID
     │       │       │       │
     ▼       ▼       ▼       ▼
  Store in  Return   Update   Set
  grid[r][c] (r,c)  quantity  null
          │
          ▼
   Refresh Grid + Stats
          │
          ▼
         END
```

---

## ✔️ Validation Rules

| Rule | Behaviour |
|------|-----------|
| Duplicate Item ID | ❌ Rejected with clear error message |
| Invalid coordinates | ❌ Rejected (must be 0–4) |
| Negative quantity | ❌ Rejected |
| Occupied slot | ❌ Rejected with existing item ID shown |
| Empty item ID/name | ❌ Rejected |

---

## 📤 Output Examples

```
Searching for Item ID: I102
Item Found → Row: 1  Column: 2  Name: Phone  Quantity: 25

Searching for Item ID: I999
Item Not Found

Warehouse Occupancy: 40.0%
Total Items: 10 | Empty Slots: 15 | Total Quantity: 455
```

---

## 👨‍💻 Author

BTech Semester 2 OOP Project  
Subject: Object-Oriented Programming with Java
