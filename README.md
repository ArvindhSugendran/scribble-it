# ✏️ Scribble – Advanced Canvas Drawing App

A modern Android application that enables users to create, manage, and replay canvas drawings with a highly scalable architecture and performance-focused design.

Built with a strong emphasis on clean architecture, reactive state management, and  Foldable & Adaptive UI systems, Scribble goes beyond a typical drawing app by incorporating features like autosave mechanism, replay playback, thumbnail generation, lifecycle-aware data handling, and responsive layouts.

## ✨ Highlights (Quick Overview)

🧩 **Clean Architecture** with modular, feature-based separation  
🧠 **Custom Metrics System** for dynamic UI scaling and adaptive layouts  
📱 **Adaptive Layouts** for foldables, split-screen, and two-pane interactive layouts with drag-resize & snap behavior  
⚡ **High-performance Canvas Drawing System** with GPU-optimized rendering, autosave mechanism, brush/stroke options, and viewport transforms  
🎬 **Replay System** for canvas drawings with interactive playback controls  
🖼️ **Thumbnail Generation** for canvas previews and optimized file storage  
⚡ **Paging 3 Integration** with manual refresh triggers and optimized Kotlin Flow handling  
🎯 **Advanced State Management** using Kotlin Flows + ViewModel
🎨 **Material 3 Theming System** with dynamic colors, custom palettes, and CompositionLocal-driven color injection  
🔄 **Smart UI Synchronization** between list, grid, and drawing screens

## 🎥 Demo

🎥 Full Demo (All Features): https://drive.google.com/drive/folders/1lmSqNPvSz2H79c18-sqjf5B40LZ52jO1?usp=sharing

⚡ Short previews below — full demo linked above

### ✦ Drawing on Canvas
https://github.com/user-attachments/assets/be79d406-ec97-4540-84c4-81a5b39e5803

### ✦ Two-Pane Adaptive Layout Interaction
https://github.com/user-attachments/assets/9dfebc1c-7238-4f73-a3d2-88e6fc2f2f5f

### ✦ Replay System
https://github.com/user-attachments/assets/552a5088-1a23-43bd-96d7-edcf3ebebb78

### ✦ Seamless transition List <-> Grid <-> Horizontal Pager
https://github.com/user-attachments/assets/79c5e881-9327-4033-b2dd-7ce648cefed3

## 🧠 Architecture

This project follows a Clean Architecture approach with **feature-based separation**, meaning each major feature (Canvas, Recycle, Onboarding, etc.) has its own clearly defined module structure for UI, ViewModel, UseCases, and Repository.
```
UI (Compose)
   ↓
ViewModel (State, Actions, Events)
   ↓
UseCases (Business Logic)
   ↓
Repository (Abstraction)
   ↓
Data Layer (Room DB, File Storage, DataStore)
```
Key Principles
```
✦	Unidirectional Data Flow (UDF)
✦	Separation of concerns
✦	Feature-based modularization for scalable code
✦	Reactive streams using Kotlin Flow
✦	Type-safe state and error handling
✦	Metric-driven, adaptive, interactive UI system
```

## 🚀 Features

### 🎨 Drawing Engine

	✦	Custom canvas rendering system with stroke-based drawing
	✦	Brush size configuration and stroke customization
	✦	Viewport transformation system for scalable drawing and navigation
	✦	Optimized rendering using structured drawing state management

### 🖥️ Custom Two-Pane Responsive Engine

This is what sets Scribble apart from standard apps.

	✦	Fully custom responsive metrics engine that calculates:
	✦	Spacing, paddings, typography scaling
	✦	Grid column counts
	✦	Visibility rules and animation thresholds
	✦	Component sizing based on:
	✦	Pane width
	✦	Screen width
	✦	Drag reveal percentage
	✦	Layout configuration
  
Implements a custom interactive pane system with:

	✦	Drag-to-resize panes
	✦	Reveal animation physics
	✦	Snap behavior and partial visibility states
	✦	Frozen metric calculations during drag
	✦	Smooth layout stability logic

⚡ Unlike Jetpack Compose’s AdaptiveScaffold:

	✦	Supports live width animation and drag resizing
	✦	Handles custom reveal percentages
	✦	Provides snap-to-position behavior
	✦	Maintains metric-driven UI architecture

Why it matters:
This system is framework-level UI engineering.
It’s more than responsive design; it’s a metric-driven, interactive adaptive layout engine that scales to any screen configuration.

### 📂 Canvas Management

	✦	Create, update, archive, and delete canvases
	✦	Recycle bin with bulk actions
	✦	Permanent deletion with lifecycle-based cleanup
	✦	Smart canvas summaries and metadata tracking

### 🔍 Search, Sort & Pagination
  
	✦	Paging 3 integration for scalable data loading
	✦	Search and filter support
	✦	Sorting by title, created date, and modified date
	✦	Smooth scrolling with efficient data flow

### 🖼️ Thumbnail Generation
  
	✦	Automatic thumbnail creation for each canvas
	✦	CPU vs IO optimized processing using coroutines
	✦	File system integration for persistent preview storage

### 📱 Adaptive UI System

  	•	Seamless transition between: List ↔ Grid ↔ Canvas
	✦	Custom Metrics + Provider pattern for responsive layouts
	✦	Supports list and grid view modes
	✦	Screen size classification (width & height aware)
	✦	Config-driven UI behavior for scalability

### 🖌️ Canvas Rendering System

	✦ Built custom drawing system using Compose Canvas
	✦ Optimized for:
	  ✦ Smooth rendering (GPU)
	  ✦ Real-time drawing interactions

### 🧭 Navigation & UX

	  ✦ Type-safe navigation
	  ✦ Custom transitions (horizontal + vertical)
	  ✦ Back handling with state-aware logic

### 🎯 UI & Design System
  
	✦	Built with Material 3 (light, dark, dynamic color)
	✦	Reusable UI components (buttons, app bars, controls)
	✦	Structured UI state management (TopBar, FAB, Dialogs, etc.)
	✦	Consistent design system across the app

### 🚪 Onboarding & Navigation
  
	✦	Type-safe navigation architecture
	✦	Onboarding flow with back stack management
	✦	Gate-based entry control for app flow
	✦	Clean navigation graph integration

### 💾 Persistence & Data Layer
  
	✦	Room database with schema export
	✦	DataStore for user preferences
	✦	Repository pattern with dependency injection (Hilt)
	✦	Clean separation: Data → Domain → UseCase → UI

### ⚙️ Tech Stack
  
	✦	Kotlin
	✦	Jetpack Compose
	✦	Material 3
	✦	Room Database
	✦	Paging 3
	✦	Hilt (Dependency Injection)
	✦	Kotlin Coroutines & Flow
	✦	DataStore Preferences
	✦	Moshi (JSON parsing)

### 🧩 Key Engineering Highlights
  
	✦	Custom canvas drawing engine with viewport transformations
	✦	Advanced replay system for drawing sessions
	✦	Optimized thumbnail generation (CPU vs IO separation)
	✦	Scalable metrics-driven adaptive UI system
	✦	Clean Result wrapper with type-safe error handling
	✦	Lifecycle-aware archive & recycle bin system
	✦	Modular and reusable UI + state architecture
	✦	Production-ready navigation and onboarding flow
	
🔥 What Makes This Project Stand Out

	✦	Goes beyond standard CRUD apps
	✦	Focus on architecture + performance + scalability
	✦	Demonstrates real-world Android engineering challenges
	✦	Built with modern Android best practices

---

This section shows **both your technical skills and the complexity of the system you built**, which is fantastic for a portfolio.

## 🏫 Learnings

This project pushed me to **explore and acquire a wide range of advanced Android and software engineering concepts**. Some highlights include:

✦ **Kotlin & Jetpack Compose** – building reactive UIs, managing state, and side-effects  
✦ **Clean Architecture & Feature-based Separation** – structuring scalable, maintainable code  
✦ **Reactive Streams & Kotlin Flow** – handling timing, events, and data pipelines efficiently  
✦ **Custom Canvas & Drawing System** – stroke-based rendering, brush/stroke configuration, viewport transformations, thumbnail generation  
✦ **Material 3 Theming & Adaptive Design** – light/dark modes, dynamic colors, metrics-driven layouts  
✦ **Custom Interactive Layout Engines** – building two-pane layouts with drag-resize, snapping, partial visibility, and smooth animation  
✦ **Metrics Engine & Layout Calculations** – dynamically scaling components based on screen, pane, and layout configuration  
✦ **Persistence & Data Handling** – Room DB, type converters, DataStore preferences, and file management  
✦ **Problem-solving & Mathematical Applications** – geometry, scaling, viewport transformations, animation math, and optimization  

> And much more — this project was essentially **framework-level UI and architecture engineering** packed into a single app.

---

## 👨‍💻 Author
Arvindh Prasadh Sugendran
