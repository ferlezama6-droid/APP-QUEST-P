# Quest Log — App nativa para Android

Este proyecto envuelve tu agenda (index.html) como una app Android real usando Capacitor,
para que el almacenamiento de tus datos viva en el espacio privado de la app —
completamente separado de Chrome, así que "Borrar datos de navegación" en Chrome
ya no puede tocarlo.

Hay dos formas de obtener el `.apk` instalable. No necesitas hacer las dos, elige una.

## Opción A (sin instalar nada en tu compu): que GitHub lo compile por ti

1. Crea un repositorio nuevo en GitHub (puede ser privado) y sube **toda esta
   carpeta** tal cual (ya trae el archivo `.github/workflows/build-apk.yml` —
   ese es el que hace la magia). No hace falta que subas `node_modules/` ni
   `android/app/build/` (ya están en `.gitignore`, GitHub los genera solo).
2. Entra a la pestaña **Actions** de tu repo en GitHub. Debería empezar a
   correr solo ("Compilar APK de Quest Log"). Si no, dale a "Run workflow".
3. Espera a que termine (unos 3-6 minutos, ícono verde ✅).
4. Entra a esa ejecución terminada, baja hasta **Artifacts**, descarga
   `quest-log-apk` (viene en un .zip, adentro está el `.apk`).
5. Pasa ese `.apk` a tu celular (por cable, Drive, WhatsApp, lo que sea) y
   ábrelo para instalarlo — Android te va a pedir activar "Instalar apps de
   orígenes desconocidos" la primera vez.

Cada vez que subas un cambio nuevo al repo, se genera un `.apk` actualizado
solo, sin que tengas que hacer nada más.

## Opción B: con Android Studio en tu compu

1. Instala **Android Studio** (gratis): https://developer.android.com/studio
2. Descomprime esta carpeta completa en tu computadora.
3. Abre Android Studio → "Open" → selecciona la carpeta `android/` (la de adentro,
   no la raíz del proyecto).
4. Espera a que termine el "Gradle Sync" la primera vez (descarga cosas de internet,
   puede tardar varios minutos).
5. Conecta tu celular por USB con "Depuración USB" activada (Ajustes → Opciones de
   desarrollador), o crea un emulador desde Android Studio.
6. Dale al botón ▶️ (Run) — se instala y abre directo en tu celular.

O, para generar el `.apk` sin correrlo directo: Build → Build App Bundle(s) / APK(s)
→ Build APK(s). Te deja el archivo en
`android/app/build/outputs/apk/debug/app-debug.apk`.

## Si quieres actualizar la app después (nuevos cambios que yo te dé)

Solo reemplaza el archivo `www/index.html` por la versión nueva, y en una
terminal dentro de esta carpeta corre:

    npx cap sync android

Eso copia el HTML actualizado dentro del proyecto Android. Luego vuelve a
compilar desde Android Studio (▶️ Run, o Build APK).

## Qué cambia respecto a la versión web/PWA

- El botón "Exportar" en Ajustes, dentro de esta app, abre el menú nativo de
  "Compartir" de Android (para mandarlo a Drive, WhatsApp, correo, etc.) en vez
  de descargar un archivo como en el navegador.
- Cada vez que guardas algo, además del guardado normal, la app deja una copia
  de respaldo silenciosa en su propio almacenamiento privado. Si la app alguna
  vez arranca vacía (por ejemplo, tras reinstalarla) pero encuentra esa copia,
  se restaura sola.
- Esto **no reemplaza** hacer respaldos manuales de vez en cuando — sigue
  protegiendo contra reinstalar la app o borrar su almacenamiento manualmente
  desde Ajustes de Android, ya que eso sí borra todo lo de la app (WebView y
  respaldo interno por igual).
