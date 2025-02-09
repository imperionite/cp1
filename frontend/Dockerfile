FROM node:22.12.0-alpine

# Create a non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set the working directory inside the container
WORKDIR /app

COPY package.json yarn.lock ./

# Install dependencies using yarn
RUN yarn install

ENV NODE_ENV=development

ENV PATH=/app/node_modules/.bin:$PATH

COPY . .

# Change ownership of the app directory to the non-root user
RUN chown -R appuser:appgroup /app

EXPOSE 5173

# Switch to the non-root user
USER appuser

CMD ["yarn", "dev"]