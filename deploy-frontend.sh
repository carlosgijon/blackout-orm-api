#!/bin/bash
# Clona (o actualiza) el frontend, lo compila con Docker y copia al directorio de nginx.

set -e

FRONTEND_DIR="$HOME/blackout-frontend"
APP_DIR="$(dirname "$0")/app"
REPO="https://github.com/carlosgijon/heavy-metal-playlist.git"

echo "── Frontend deploy ──────────────────────────────────────"

# 1. Clonar o actualizar el repo
if [ -d "$FRONTEND_DIR/.git" ]; then
  echo "→ Actualizando repo..."
  git -C "$FRONTEND_DIR" pull
else
  echo "→ Clonando repo..."
  git clone "$REPO" "$FRONTEND_DIR"
fi

# 2. Build con Node 22 en Docker (no necesita Node instalado en el servidor)
echo "→ Compilando Angular (production)..."
docker run --rm \
  -v "$FRONTEND_DIR":/app \
  -w /app \
  node:22-alpine \
  sh -c "npm ci && npx ng build --configuration production"

# 3. Copiar el build a la carpeta que sirve nginx
echo "→ Copiando build a $APP_DIR..."
mkdir -p "$APP_DIR"
cp -r "$FRONTEND_DIR/dist/blackout-orm/browser/." "$APP_DIR/"

echo "✓ Frontend desplegado en $APP_DIR"
echo "── Listo ────────────────────────────────────────────────"
