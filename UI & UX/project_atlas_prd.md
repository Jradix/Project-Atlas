Project Atlas: UI/UX Engineering Specification
Core Design Philosophy: Industrial Minimalism. Every element must serve a functional purpose. High contrast, high data density, and zero "fluff."

1. Visual Identity & Tokens
- Theme: Hard Dark Mode (OLED Optimized).
- Color Palette:
    - Background (Base): #0D0D0D (Pure Black).
    - Surface (Cards/Modals): #1A1A1A (Deep Grey).
    - Primary Accent: #00E676 (Neon Green) — Use for "Success," "Completed," and "Record Broken."
    - Secondary Accent: #2979FF (Electric Blue) — Use for "Active State" and "Action Required."
    - Text (High Emphasis): #FFFFFF (Pure White).
    - Text (Low Emphasis): #888888 (Medium Grey).
- Border Radius: Strict 8dp to 12dp.
- Typography: System Sans-Serif (Inter/Roboto). Bold weights, Medium labels.

2. Key Screen Architectures
A. The Dashboard: Header, "Pulse" Graph (Line Chart, Electric Blue, no grid), Recent Activity (High-density cards), Neon Green FAB "Start Workout".
B. The Active Workout: Timer, 2px progress bar, Exercise Cards with "Last performance", Table layout ([Set #], [Previous], [kg], [Reps], [Checkmark]), Neon Green success state.
C. Exercise Library: Search Bar, Muscle filter chips, List Item with thumbnails or dashed-line "Missing Image" state.
D. Modal/Overlay: Rest Timer bottom-sheet, Finish Workout Summary.

3. UX Rules: Button scale effect (0.98x), numeric keypad default, Wireframe-style empty states.