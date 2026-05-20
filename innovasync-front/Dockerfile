# === ETAPA 1: Construcción (Build) ===
FROM node:20-alpine AS build
WORKDIR /app

# Copiar archivos de dependencias para aprovechar la caché de Docker
COPY package*.json ./
RUN npm install

# Copiar el resto del código y compilar el proyecto
COPY . .
RUN npm run build -- --configuration=production

# === ETAPA 2: Servidor de Producción ===
FROM nginx:1.25-alpine

# Copiar los archivos compilados desde la etapa de build al directorio de Nginx
# NOTA: Asegúrate de que la ruta 'dist/innovasync' coincida con tu angular.json
COPY --from=build /app/dist/innovasync/browser /usr/share/nginx/html

# Copiar configuración personalizada de Nginx para manejar el enrutamiento de Angular
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]