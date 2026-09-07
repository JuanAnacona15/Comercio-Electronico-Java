# PCE — Sistema de Comercio Electrónico

> Proyecto universitario desarrollado en **Java** con interfaz gráfica **Swing**, aplicando los principios de **POO**, **Clean Code** y el patrón **MVC**.

---

## Tabla de contenidos

- [Descripción](#descripción)
- [Características](#características)
- [Requisitos](#requisitos)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Cómo ejecutar](#cómo-ejecutar)
- [Credenciales de prueba](#credenciales-de-prueba)
- [Flujo de uso](#flujo-de-uso)
- [Arquitectura](#arquitectura)
- [Modelo de datos](#modelo-de-datos)

---

## Descripción

PCE es un sistema de comercio electrónico de escritorio que gestiona la venta de **productos físicos y digitales**. Permite a los clientes navegar el catálogo, agregar productos a un carrito, generar pedidos y realizar pagos con tarjeta de crédito o cuenta digital. Los administradores pueden gestionar el inventario en tiempo real.

No utiliza base de datos: los datos se mantienen **en memoria mediante vectores (`ArrayList`)** durante la ejecución del programa.

---

## Características

### Cliente
- ✅ Registro e inicio de sesión
- ✅ Catálogo de productos con tarjetas visuales (badge FÍSICO / DIGITAL)
- ✅ Indicador de stock en tiempo real (agotado, últimas unidades, disponible)
- ✅ Carrito de compras con tabla de ítems y cálculo automático de subtotal + IVA 19%
- ✅ Proceso de pago con **Tarjeta de Crédito** o **Cuenta Digital**
- ✅ Comprobante de transacción (APROBADO / RECHAZADO)
- ✅ Historial de pedidos

### Administrador
- ✅ Panel de gestión de inventario
- ✅ Agregar nuevos productos al catálogo
- ✅ Actualizar stock de productos existentes
- ✅ Eliminar productos del catálogo

---

## Requisitos

| Requisito | Versión mínima |
|---|---|
| Java JDK | **21** |
| NetBeans IDE | 19+ (recomendado) |

> ⚠️ El proyecto está configurado con `javac.source=21` y `javac.target=21`. Si tu JDK es diferente, ajusta esos valores en [`nbproject/project.properties`](nbproject/project.properties).

---

## Estructura del proyecto

```
PCE/
├── src/
│   ├── Main.java                        ← Punto de entrada
│   ├── Modelo/                          ← Capa de dominio (no modificar)
│   │   ├── Usuario.java
│   │   ├── Cliente.java
│   │   ├── Administrador.java
│   │   ├── Producto.java
│   │   ├── TipoProducto.java            ← Enum: FISICO, DIGITAL
│   │   ├── EstadoProducto.java          ← Enum: DISPONIBLE, AGOTADO
│   │   ├── CarritoDeCompra.java
│   │   ├── ItemCarrito.java
│   │   ├── Pedido.java
│   │   ├── Pago.java                    ← Interfaz de pago
│   │   ├── PagoTragetaCredito.java
│   │   ├── PagoCuentaDigital.java
│   │   ├── Comprobante.java
│   │   └── EstadoTransaccion.java       ← Enum: APROBADO, RECHAZADO
│   ├── Controlador/                     ← Capa de lógica de negocio
│   │   ├── ControladorPrincipal.java    ← Vectores en memoria + datos de ejemplo
│   │   ├── ControladorProductos.java    ← CRUD del catálogo
│   │   ├── ControladorCarrito.java      ← Operaciones del carrito
│   │   └── ControladorPedido.java       ← Generación de pedido y pago
│   └── Vista/                           ← Capa de interfaz gráfica (Swing)
│       ├── Estilos.java                 ← Sistema de diseño centralizado
│       ├── Refrescable.java             ← Interfaz para actualización de paneles
│       ├── VentanaPrincipal.java        ← JFrame con CardLayout
│       ├── PanelLogin.java
│       ├── PanelRegistro.java
│       ├── PanelCatalogo.java
│       ├── PanelCarrito.java
│       ├── PanelPago.java
│       ├── PanelComprobante.java
│       ├── PanelAdminProductos.java
│       └── componentes/
│           └── TarjetaProducto.java     ← Componente visual de producto
├── nbproject/                           ← Configuración de NetBeans
├── build.xml
└── README.md
```

---

## Cómo ejecutar

### Desde NetBeans
1. Abrir NetBeans → **File → Open Project** → seleccionar la carpeta `PCE`
2. Click derecho en el proyecto → **Clean and Build** (`Shift + F11`)
3. **Run Project** (`F6`)

### Desde la terminal
```bash
# Compilar
find src -name "*.java" | xargs javac --release 21 -encoding UTF-8 -d build/classes

# Ejecutar
java -cp build/classes Main
```

---

## Credenciales de prueba

Los datos de ejemplo se cargan automáticamente al iniciar la aplicación desde `ControladorPrincipal.cargarDatosDePrueba()`.

| Rol | Correo | Contraseña |
|---|---|---|
| **Administrador** | `admin@pce.com` | `admin1234` |
| **Cliente** | `juan@example.com` | cualquier texto |

> Para modificar o eliminar los datos de ejemplo, edita el método `cargarDatosDePrueba()` en [`src/Controlador/ControladorPrincipal.java`](src/Controlador/ControladorPrincipal.java). Para desactivarlos completamente, comenta la llamada en el constructor.

---

## Flujo de uso

```
Inicio
  │
  ├─► [Login] ──────────────────────────────────► [Admin] Gestión de inventario
  │     │
  │     └─► [Registro] ──► [Login]
  │
  └─► [Catálogo] ──► Agregar productos al carrito
         │
         └─► [Carrito] ──► Ver ítems, eliminar, ver totales con IVA
                │
                └─► [Pago] ──► Elegir método (Tarjeta / Cuenta Digital)
                       │
                       └─► [Comprobante] ──► Ver resultado + historial
                              │
                              └─► [Catálogo] (volver a comprar)
```

---

## Arquitectura

El proyecto sigue el patrón **MVC (Modelo - Vista - Controlador)**:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **Modelo** | `Modelo` | Entidades del dominio, reglas de negocio puras |
| **Controlador** | `Controlador` | Coordinación, lógica de aplicación, datos en memoria |
| **Vista** | `Vista` | Interfaz gráfica Swing, sin lógica de negocio |

La navegación entre pantallas se realiza mediante un `CardLayout` en `VentanaPrincipal`. Cada panel implementa la interfaz `Refrescable` para actualizarse al ser mostrado.

---

## Modelo de datos

```
Usuario (abstracta)
  ├── Cliente          → tiene CarritoDeCompra, lista de Pedidos
  └── Administrador    → puede gestionar stock con gestionarProducto()

Producto
  ├── TipoProducto     → FISICO | DIGITAL
  └── EstadoProducto   → DISPONIBLE | AGOTADO

CarritoDeCompra        → contiene List<ItemCarrito>
ItemCarrito            → referencia Producto + cantidad

Pedido                 → subtotal, IVA 19%, total
Pago (interfaz)
  ├── PagoTragetaCredito
  └── PagoCuentaDigital

Comprobante
  └── EstadoTransaccion → APROBADO | RECHAZADO
```

---

## Notas de implementación

- **Sin base de datos**: todos los datos viven en `ArrayList<>` dentro de `ControladorPrincipal` mientras el programa está en ejecución.
- **Validación de inventario**: un producto con stock 0 no puede agregarse al carrito (`verificarDisponibilidad()` del modelo). El botón "Agregar" se desactiva visualmente.
- **IVA**: el 19% se calcula automáticamente en `Pedido.calcularImpuesto()` y se refleja en el carrito y en el panel de pago.
- **Precios**: formateados en pesos colombianos (COP), separados por puntos (ej: `$ 250.000`).
