# CapucareApp

Proyecto correspondiente a la Evaluación Parcial 4 de Desarrollo de Aplicaciones Móviles (**DSY1105**). Esta aplicación móvil nativa permite gestionar de forma ágil y centralizada el inventario, control de stock crítico, escaneo de códigos y trazabilidad de movimientos de insumos clínicos y operativos.

## 1. Integrantes del Equipo
* **Constanza Carrasco:** Backend, Base de Datos (MySQL) y Microservicios REST (Spring Boot).
* **Thian Pereira:** Interfaz de Usuario (Jetpack Compose), API Externa (`mindicador.cl`) y Localización (Multi-idioma).
* **Jesús Serrano:** Arquitectura (MVVM), Persistencia Local (Room/Corrutinas), Cliente de Red (Retrofit), Testing (QA) y Release.

## 2. Arquitectura y Funcionalidades Principales
* **Interfaz Nativa:** Desarrollada íntegramente con **Jetpack Compose** y Material 3, con soporte multi-idioma (`values` / `values-en`).
* **Arquitectura MVVM:** Separación estricta de responsabilidades entre vistas (`ui/screens`), estado (`ViewModel` con `viewModelScope` y `Dispatchers.IO`), repositorios (`data/repository`) y fuentes de datos.
* **Persistencia Local y Caché:** Implementación de base de datos local con **Room** (`cappucare_database`) para garantizar disponibilidad de datos y fluidez de interfaz sin bloqueo del hilo principal, complementada con **OkHttp/Retrofit** para consumo de red.
* **Escáner y Alertas:** Integración con Google Play Services Code Scanner para lectura rápida de códigos de insumos y notificaciones locales ante niveles de stock crítico.
* **Integración de Servicios:** Sincronización CRUD con microservicios propios en **Spring Boot** (conectados a **MySQL**) y consumo de indicadores económicos en tiempo real mediante API externa sin afectar el rendimiento local.

## 3. Endpoints Utilizados

**API Externa (Indicadores Económicos - `mindicador.cl`):**
* `GET` `https://mindicador.cl/api` — Obtiene los indicadores económicos diarios (UF, UTM, Dólar, Euro) para visualización en la pantalla de indicadores.

**Microservicios Propios (Spring Boot - Puerto `8080`):**

*Gestión de Insumos (`/api/insumos`):*
* `GET` `/api/insumos` — Lista todos los insumos registrados.
* `GET` `/api/insumos/{id}` — Obtiene el detalle de un insumo específico por su ID.
* `POST` `/api/insumos` — Registra un nuevo insumo en el inventario.
* `PUT` `/api/insumos/{id}` — Actualiza los datos o stock de un insumo existente.
* `DELETE` `/api/insumos/{id}` — Elimina un insumo del sistema.

*Historial de Movimientos (`/api/movimientos`):*
* `GET` `/api/movimientos` — Lista el historial completo de entradas y salidas de stock.
* `GET` `/api/movimientos/{id}` — Consulta un movimiento específico por su ID.
* `POST` `/api/movimientos` — Registra un nuevo movimiento de inventario.
* `PUT` `/api/movimientos/{id}` — Actualiza un registro de movimiento.
* `DELETE` `/api/movimientos/{id}` — Elimina un movimiento del historial.

## 4. Pasos para Ejecutar

### A. Backend (Spring Boot + MySQL)
1. Asegurarse de tener activo el servicio de **MySQL** en el puerto `3306` (la base de datos `capucare_db` se genera automáticamente mediante Hibernate `ddl-auto=update`).
2. Ejecutar el proyecto backend desde la raíz con Maven (`./mvnw spring-boot:run`) o desde su IDE; el servidor escuchará en `0.0.0.0:8080`.

### B. Aplicación Móvil (Android)
1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/Jesus-Gabriel-Serrano-Moncada/CapucareApp.git](https://github.com/Jesus-Gabriel-Serrano-Moncada/CapucareApp.git)
