FROM node 16.20.1
WORKDIR /app
COPY package*.json ./
RUN npm install -g serve
COPY . .
RUN npm run build
