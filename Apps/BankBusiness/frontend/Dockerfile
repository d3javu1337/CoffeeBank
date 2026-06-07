FROM node:26.0.0-slim AS builder
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . ./
RUN npm run build

FROM nginx:1.30-trixie
EXPOSE 80
COPY --from=builder /app/dist /usr/share/nginx/html
CMD ["nginx", "-g", "daemon off;"]