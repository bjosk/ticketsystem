# Build the Vue app
FROM node:18-alpine AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Serve with NGINX
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Replace default NGINX config. Redirects api calls to :8080
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
