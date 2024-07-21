<p align="center">
  <img src="https://raw.githubusercontent.com/piertv21/gnam.my/main/assets/favicon.png" alt="Logo" width="80" height="80">
  <h3 align="center">Gnam.my</h3>
  <p align="center">
    A simple cooking-related social for Android.
  </p>
</p>

##
<br />

<p align="center">
  <img width="150" alt="image" src="https://github.com/user-attachments/assets/bf857b3d-3c98-46cd-9763-8c22c7be2843">
  <img width="150" alt="image" src="https://github.com/user-attachments/assets/94ae5a62-84f7-41ec-aa20-cfd5801db61f">
  <img width="150" alt="image" src="https://github.com/user-attachments/assets/42d6d459-2135-4b3f-8e2c-ed0c7e73bf01">
</p>

## Installation & Setup

**1. Clone the repository**<br />
```
git clone https://github.com/piertv21/gnammy-mobile
```

**2. Start Docker**<br />
Navigate to "app_backend/scripts" and start the correct script according to your operating system.
In case you are using Windows run "start-containers.bat".
```
.\start-containers.bat
```

**3. Import the project in Android Studio**<br />
Open Android Studio and import the project, and wait for Gradle to build.

**4. Set the correct backend IP**<br />
Open "AppModule.kt" and edit "backendSocket" according to your docker backend IP.
Example: if you are on Windows open cmd and type "ipconfig" and copy the first IPv4 address of the WSL card.
The backend port is 3000.
```
val backendSocket = "http://192.168.178.67:3000"
```

**5. Run the App**<br />
Enjoy the app.
