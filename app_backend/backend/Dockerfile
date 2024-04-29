# Compilazione dell'immagine Docker per il backend
FROM node:lts-alpine3.19 AS build

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install --omit=dev

COPY . .

RUN npm run build

RUN npx prisma generate

# Creazione dell'immagine Docker per il backend
FROM node:lts-alpine3.19

WORKDIR /usr/src/app

COPY --from=build /usr/src/app/package*.json ./
COPY --from=build /usr/src/app/dist ./dist
COPY --from=build /usr/src/app/prisma ./prisma
COPY --from=build /usr/src/app/.env ./

RUN npm install --omit=dev

EXPOSE 3000

CMD [ "node", "dist/app.js" ]