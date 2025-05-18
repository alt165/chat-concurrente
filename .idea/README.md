# 🧵 Chat Multicliente en Java

Proyecto final para la materia **Sistemas Distribuidos y Paralelos**.

Este sistema permite la comunicación entre múltiples clientes a través de un servidor central utilizando **Sockets TCP** y **concurrencia con hilos**. Se incluye control de acceso, historial de mensajes en memoria y un límite definido de usuarios simultáneos.

---

## ⚙️ Tecnologías utilizadas

- Java SE 8+
- Sockets TCP/IP
- Concurrencia con Threads
- `CopyOnWriteArrayList` para manejo seguro de múltiples hilos

---

## 🚀 Características del servidor

- Manejo multithreaded de conexiones entrantes
- Hasta **100 usuarios simultáneos** (valor configurable)
- Rechazo automático de conexiones excedentes con mensaje personalizado
- Historial de mensajes en memoria (próximamente)
- Comunicación en tiempo real (broadcast)
- Mensajes enviados y recibidos desde la consola del cliente

---

## 🖥️ Estructura del proyecto

```
📦 chat-java
 ┣ 📜 ChatServer.java
 ┣ 📜 ClientHandler.java
 ┣ 📜 ChatClient.java
 ┗ 📜 README.md
```

---

## 🧪 Cómo ejecutar

### 🖥️ Ejecutar el servidor

```bash
javac ChatServer.java ClientHandler.java
java ChatServer
```

> El servidor escuchará en el puerto `12345` por defecto.

---

### 💬 Ejecutar un cliente

```bash
javac ChatClient.java
java ChatClient
```

> Puedes abrir múltiples terminales o computadoras para conectarte al mismo servidor.

---

## 📌 Parámetros configurables

| Parámetro            | Valor por defecto | Descripción                         |
|----------------------|-------------------|-------------------------------------|
| Puerto del servidor  | `12345`           | Puerto TCP donde escucha el servidor |
| Máximo de usuarios   | `100`             | Número máximo de clientes concurrentes |
| Tamaño del historial | `20`              | (Próximamente) Cantidad de mensajes almacenados |

---

## 🧠 Conceptos aplicados

- Concurrencia y sincronización
- Programación orientada a objetos
- Comunicación en red TCP/IP
- Manejo de múltiples hilos (`Thread`)
- Validación y control de acceso en sistemas distribuidos

---

## 🔜 Mejoras futuras

- [ ] Historial persistente de mensajes
- [ ] Comandos especiales (`/usuarios`, `/salir`)
- [ ] Interfaz gráfica con JavaFX
- [ ] Cifrado con SSL/TLS
- [ ] Autenticación de usuarios

---

## 🧑‍💻 Autor

- **Jose Lambrechts**  
Estudiante de Ingenieria en Computacion
Universidad Nacional de Rio Negro

---

## 📄 Licencia

Este proyecto es libre para uso académico y educativo.
