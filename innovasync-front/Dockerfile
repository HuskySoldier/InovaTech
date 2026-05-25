# === ETAPA 1: Construcción (Build) ===
FROM node:20-alpine AS build
WORKDIR /app

# Copiar archivos de dependencias para aprovechar la caché de Docker
COPY package*.json ./

# Instalar dependencias (se añade --legacy-peer-deps para evitar conflictos comunes en Angular)
RUN npm install --legacy-peer-deps

# Copiar el resto del código fuente
COPY . .

# Compilar el proyecto usando directamente el CLI de Angular
# En Angular 17+, la configuración 'production' es el por defecto
RUN npx ng build --configuration production

# === ETAPA 2: Servidor de Producción ===
FROM nginx:1.25-alpine

# Limpiar el directorio HTML por defecto de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiar los archivos compilados del cliente (browser) al servidor Nginx
# El builder "@angular/build:application" genera la salida en dist/[proyecto]/browser
COPY --from=build /app/dist/innovasync/browser /usr/share/nginx/html

# Copiar tu configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]