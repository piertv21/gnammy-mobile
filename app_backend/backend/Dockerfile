FROM node:lts-alpine3.19

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install --omit=dev

COPY src ./src
COPY prisma ./prisma
COPY .env ./

RUN npx prisma generate

EXPOSE 3000

CMD ["node", "src/app.js"]