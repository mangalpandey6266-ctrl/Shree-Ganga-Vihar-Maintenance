SHREE GANGA VIHAR MAINTENANCE – PHONE BUILD EDITION

This project is ready for a phone-only cloud build. No PC/laptop is required to BUILD the APK once the project is uploaded to GitHub.

APP FEATURES
- 72 flats: Ground 029–052, First 129–152, Second 229–252
- Receive Maintenance payment against a flat
- Add Expense
- Automatic total Received / Spent / Balance
- Current-month Pending Maintenance list
- WhatsApp share button for the pending reminder
- Monthly Report
- Data saved locally on the Android phone

PHONE-ONLY APK BUILD (GitHub)
1. On your phone, open github.com and sign in/create a GitHub account.
2. Create a NEW repository, for example: Shree-Ganga-Vihar-Maintenance-App.
3. Upload the CONTENTS of the ShreeGangaViharApp folder into the repository (including the .github folder).
4. Open the repository → Actions → Build Shree Ganga Vihar APK.
5. Run the workflow if GitHub asks for confirmation.
6. After it finishes successfully, open the workflow run → Artifacts → ShreeGangaViharMaintenance-APK.
7. Download the ZIP artifact on the phone, extract it, and install app-debug.apk.

IMPORTANT
- The GitHub workflow builds a DEBUG APK; Android may ask you to allow installation from your browser/file manager.
- WhatsApp sharing opens WhatsApp with the reminder prepared; you still press Send.
- This app currently stores data locally on the phone.

If GitHub mobile upload is difficult, use GitHub in Chrome with “Desktop site” enabled.
