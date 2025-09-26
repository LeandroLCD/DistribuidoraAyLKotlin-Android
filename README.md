# 📱 DistribuidoraAyL

Aplicación Android desarrollada en **Kotlin** con un enfoque moderno, basada en **Jetpack Compose** para la interfaz de usuario, arquitectura limpia (UI–Domain–Data) y con inyección de dependencias mediante **Hilt**. Diseñada para ser escalable, mantenible y aprovechar las últimas tecnologías del ecosistema Jetpack.

---

## 🚀 Información General
- **Lenguaje:** Kotlin (jvmTarget = JVM 17)
- **minSdk:** 24
- **targetSdk:** 36
- **compileSdk:** 36
- **Versión de la App:** `1.0.0` (versionCode = 1)

---

## 🏗️ Arquitectura
<div align="center">
  <img src="https://github.com/LeandroLCD/DistribuidoraAyLKotlin-Android/blob/master/img/arquitecture.png"> 
</div>
El proyecto sigue una **arquitectura limpia** dividiendo el código en tres capas principales:
- **UI:** Interfaz de usuario construida con Jetpack Compose.
- **Domain:** Lógica de negocio y casos de uso.
- **Data:** Gestión de datos locales y remotos.

### 🔧 Patrones y principios
- **Inyección de dependencias:** Implementada con **Hilt**.
- **Separación de responsabilidades:** Mejorando la mantenibilidad y escalabilidad.

---

## 📚 Tecnologías y Librerías Clave

### 🎨 Interfaz de Usuario (UI)
- **Jetpack Compose** (Material 3, Material Icons, Tooling, Graphics).
- **ConstraintLayout Compose** → layouts flexibles.
- **Lottie Compose** → animaciones fluidas.
- **Form Builder** → creación rápida de formularios.
- **Coil Compose** → carga de imágenes.
- **Accompanist Pager** → carruseles y paginación en UI.

### 🧭 Navegación
- **Navigation Compose** → manejo de rutas y pantallas.
- **Hilt Navigation Compose** → integración de navegación con Hilt.

### 💾 Gestión de Datos
- **Room** (Runtime, KTX, Compiler, Paging).
- **Paging Compose y Runtime KTX** → carga eficiente de listas.
- **DataStore Preferences** → almacenamiento seguro de preferencias.
- **AndroidX Preference KTX**.

### 🌐 Red
- **Retrofit** + **Gson Converter** → consumo de APIs.
- **OkHttp & Logging Interceptor** → peticiones HTTP.
- **Kotlin Serialization JSON** → serialización y deserialización.

### ☁️ Servicios Google y Firebase
- **Firebase BOM**.
- **Firebase Auth** → autenticación.
- **Firebase Firestore** → base de datos en la nube.
- **Firebase Crashlytics** → reporte de fallos.
- **Firebase Remote Config** → configuración remota.
- **Google Play Services Auth** → autenticación con Google.
- **Google Identity Services (googleid)**.

### 📷 Cámara
- **CameraX** (core, camera2, lifecycle, view, extensions).

### 🛠️ Utilidades
- **iText7 Core** → generación de PDFs.
- **Thermal Printer** → impresión de contenido.
- **Barcode Scanner** → escaneo de códigos de barras.
- **DateTime** y **ThreeTenABP** → manejo de fechas y horas.
- **Lifecycle-runtime-ktx & LiveData**.
- **Kotlin Reflect**.
- **AndroidX Startup**.
- **WorkManager** → tareas en segundo plano.

### 🧪 Pruebas
- **JUnit** → pruebas unitarias.
- **AndroidX JUnit & Espresso** → pruebas de instrumentación.
- **Compose UI Test** → pruebas de UI con Jetpack Compose.

---

### 🎥 Videos del Proceso de Construcción
Durante el desarrollo del proyecto, se documentasto el proceso en una serie de videos en YouTube.
<div align="center">
  
[![YouTube](https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white)](https://www.youtube.com/watch?v=B1ccFA48fv0&list=PL3S1TIGE5HRSHyNFvaeqvOST8YmgA41U3)

</a>
</div>

## 📦 Instalación y Ejecución
1. Clona este repositorio:
   ```bash
   git clone https://github.com/tuusuario/DistribuidoraAyL.git
   ```
2. Abre el proyecto en **Android Studio**.
3. Configura un emulador o conecta un dispositivo físico.
4. Ejecuta la app con:
   ```bash
   ./gradlew installDebug
   ```

---

## 🤝 Contribución
¡Las contribuciones son bienvenidas! 🎉

1. Haz un fork del repositorio.
2. Crea una nueva rama: `git checkout -b feature/nueva-funcionalidad`.
3. Realiza tus cambios y haz commit.
4. Haz push a tu rama.
5. Abre un Pull Request.

---


