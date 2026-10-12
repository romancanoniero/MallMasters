# Mall Masters — Plan maestro v2.0

Fecha: 2026-10-12. Estado: arquitectura propuesta, no implementada.
Repositorio: romancanoniero/MallMasters

## Objetivo
SaaS multi-tenant para digitalizar shoppings mediante LiDAR, administrar gemelos digitales navegables, ofrecer busqueda y rutas a visitantes, geolocalizacion de amigos con consentimiento y monetizacion contextual. ION Orchard (Singapur) es referencia visual y escenario de simulacion, NO mapa real validado.

## Decisiones arquitectonicas
- Scanner iOS nativo: Swift, SwiftUI, ARKit + LiDAR; RoomPlan complementario donde aplique.
- Visitor: Kotlin Multiplatform + Compose Multiplatform para iOS y Android. Modulos compartidos para dominio, busqueda, rutas, networking, almacenamiento offline y reglas de privacidad.
- Renderizador 3D: decision pendiente de benchmark entre visor nativo integrado y Three.js embebido; mantener API de escena y formato GLB/glTF desacoplados. Compose no es un motor 3D.
- Acceso inmediato: Web/PWA React + Three.js por QR. App Clip iOS ligero en SwiftUI para acciones acotadas. Android utiliza web instantanea, sin depender de Google Play Instant.
- Backend: Kotlin + Ktor para API, autenticacion y servicios comerciales; PostgreSQL/PostGIS, Redis, WebSockets.
- Workers espaciales: Python + Open3D y librerias de optimizacion/alineacion evaluadas con datasets reales. GLB/glTF optimizados y grafo de navegacion independiente.
- Panel administrativo: React + TypeScript.

## Productos y flujos
Scanner: registrar tenant -> edificio -> planta -> sector -> captura -> QA -> exportacion local -> subida cifrada -> alineacion y fusion -> validacion humana -> publicar mapa versionado.
Visitor: identificar shopping por QR -> mapa rapido -> buscar comercio -> ruta accesible por plantas -> descarga offline; localizacion interior por QR, IMU, AR visual y radios donde existan, indicando incertidumbre.
Social: amigos opt-in, seleccion individual de destinatarios, tiempo de expiracion, revocacion, ubicacion aproximada o precisa. En segundo plano sujetarse a limites iOS/Android.
Smart Proximity: geofencing 3D, entrada/salida con hysteresis, cooldown, reglas, WebSocket online, APNs/FCM push; distancia de ruta entre plantas, no solo euclidea.

## Seguridad
OIDC, MFA de operadores, roles RBAC, scopes por tenant en backend, filas y objetos segregados, aislamiento tambien en cola de workers y cache, TLS/WSS, cifrado en reposo, secretos externos al repo, logs/auditoria y pruebas de aislamiento. Distinguir modelos privados de mapas publicos. No permitir a malls consultar ubicaciones individuales sin base legal y consentimiento especifico. Backups cifrados fuera de la VPS y pruebas de restauracion.

## Estructura de monorepo
apps/scanner-ios/
apps/visitor-compose/
apps/visitor-web/
apps/visitor-appclip-ios/
apps/admin-web/
shared/domain/
shared/navigation/
shared/search/
shared/offline-maps/
shared/location/
shared/networking/
backend/api-ktor/
backend/realtime/
backend/geofencing/
backend/notifications/
backend/billing/
backend/spatial-workers/
infrastructure/contabo/
infrastructure/docker/
infrastructure/ci/
docs/
tests/

## Desarrollo desde iPhone, sin Mac local
GitHub -> CI con runner macOS (p.ej. Codemagic) -> firma Apple -> TestFlight en iPhone para Scanner y Visitor. Android: APK interno; web: deploy staging. Requiere cuenta Apple Developer, App Store Connect, certificados/firmado. Los runners macOS remotos siguen siendo necesarios para iOS. Incluir diagnosticos de ARKit exportables. No almacenar certificados ni secretos en Git.

## Contabo
Ruta propuesta /opt/mallmasters, Docker Compose con redes y recursos acotados; revisar previamente VPS, proxy, dominios, CPU/RAM, volumenes, logs, otros contenedores y backups. Sin tocar otras apps sin inventario y autorizacion. Procesamiento espacial pesado via workers limitados o capacidad externa. Staging separado de produccion.

## Roadmap orientativo y criterios de aceptacion
F0 (semana 1): auditoria Contabo, credenciales/entitlements Apple, CI Mac remoto, skeleton KMP y contrato API, smoke build TestFlight/Android.
F1 (semanas 2-4): visor 3D ION Orchard simulado, busqueda, grafo de pisos y rutas basicas en web y Compose; benchmark renderer.
F2 (semanas 4-8): Scanner LiDAR real, carga de sesiones, QA y procesamiento 3D inicial; demostrar sector real medido.
F3 (semanas 8-12): SaaS de tenants, panel comercial, mapa offline, ruteo accesible, QR, permisos y pruebas de seguridad.
F4 (semanas 12-16): promociones y anuncios medibles, dashboard agregado y comercializacion piloto.
F5 (semanas 16-22): pruebas de posicionamiento interior, amigos, geofencing, push, App Clip y optimizacion.

Las fechas son hipotesis, no promesa. Prioridad: prototipo compilable, scanner verificable, mapas y rutas antes de posicionamiento social complejo.

## Monetizacion a validar
Setup de digitalizacion por superficie/complejidad; SaaS Starter/Pro/Enterprise por sede; campanas patrocinadas identificadas; analitica agregada; white-label, kioscos y APIs. Hipotesis de precios, no validadas: Starter USD499/mes, Pro USD1490/mes, Enterprise USD3500+/mes; onboarding USD3000-25000+. Validar costo real de mapeo/actualizacion, conversion y valor comercial.

## Innovacion
- Scanner con mapa de cobertura, incertidumbre y rescaneo por sectores.
- Exterior 3D reconocible + cortes interiores seleccionables.
- Busqueda por intencion y rutas optimizadas entre tiendas; catalogos solo con datos corroborados.
- Comparticion granular de amigos con consentimiento y vencimiento.
- Geofencing con plantas y distancia real recorrible.
- Atribucion de campanas con eventos verificables y proteccion de privacidad.
- Web sin instalacion como principal acceso masivo.

## Bloqueantes
iPhone/iPad modelo LiDAR, Apple Developer vigente, acceso y limites VPS Contabo, dominio de staging, permisos para capturar un sitio real, benchmark rendimiento 3D. Evitar asumir precision de SLAM o ubicacion indoor sin medirla.
