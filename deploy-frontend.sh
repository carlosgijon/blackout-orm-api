#!/bin/bash
# Clona (o actualiza) el frontend, lo compila con Docker y copia al directorio de nginx.

set -e

FRONTEND_DIR="$HOME/blackout-frontend"
APP_DIR="$(dirname "$0")/app"
REPO="https://github.com/carlosgijon/heavy-metal-playlist.git"

echo "── Frontend deploy ──────────────────────────────────────"

# 1. Arreglar permisos si el repo fue creado por root, luego reclonear limpio
if [ -d "$FRONTEND_DIR" ]; then
  echo "→ Arreglando permisos y reclonando..."
  docker run --rm -v "$FRONTEND_DIR":/repo alpine chown -R $(id -u):$(id -g) /repo
  rm -rf "$FRONTEND_DIR"
fi
echo "→ Clonando repo..."
git clone "$REPO" "$FRONTEND_DIR"

# 2. Build con Node 22 en Docker (no necesita Node instalado en el servidor)
echo "→ Compilando Angular (production)..."
docker run --rm \
  -v "$FRONTEND_DIR":/app \
  -w /app \
  node:22-alpine \
  sh -c "npm ci && npx ng build --configuration production"

# 3. Copiar el build a la carpeta que sirve nginx, con permisos correctos
echo "→ Copiando build a $APP_DIR..."
mkdir -p "$APP_DIR"
docker run --rm -v "$FRONTEND_DIR/dist/blackout-orm/browser":/src -v "$APP_DIR":/dst alpine sh -c "cp -r /src/. /dst/ && chmod -R a+rX /dst"

echo "✓ Frontend desplegado en $APP_DIR"
echo "── Listo ────────────────────────────────────────────────"
