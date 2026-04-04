#!/bin/bash
# Clona (o actualiza) el frontend, lo compila con Docker y copia al directorio de nginx.

set -e

FRONTEND_DIR="$HOME/blackout-frontend"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
APP_DIR="$SCRIPT_DIR/app"
REPO="https://github.com/carlosgijon/heavy-metal-playlist.git"

echo "── Frontend deploy ──────────────────────────────────────"
echo "→ APP_DIR: $APP_DIR"

# 1. Clonar o actualizar el repo
if [ -d "$FRONTEND_DIR/.git" ]; then
  echo "→ Actualizando repo..."
  git -C "$FRONTEND_DIR" fetch origin
  git -C "$FRONTEND_DIR" reset --hard origin/master
else
  echo "→ Clonando repo..."
  git clone "$REPO" "$FRONTEND_DIR"
fi

echo "→ Commit: $(git -C "$FRONTEND_DIR" log --oneline -1)"

# 2. Build con Node 22 en Docker (no necesita Node instalado en el servidor)
echo "→ Compilando Angular (production)..."
docker run --rm \
  -v "$FRONTEND_DIR":/app \
  -w /app \
  node:22-alpine \
  sh -c "npm ci --prefer-offline 2>/dev/null || npm ci && node_modules/.bin/ng build --configuration production"

# 3. Copiar el build a la carpeta que sirve nginx
echo "→ Copiando build a $APP_DIR..."
mkdir -p "$APP_DIR"
rm -rf "${APP_DIR:?}"/*
cp -r "$FRONTEND_DIR/dist/blackout-orm/browser/." "$APP_DIR/"

echo "→ Archivos en $APP_DIR:"
ls "$APP_DIR" | head -10

echo "✓ Frontend desplegado en $APP_DIR"
echo "── Listo ────────────────────────────────────────────────"
