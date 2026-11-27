# Primeira etapa: utiliza Gradle com JDK 21 para compilar a aplicação.
FROM gradle:8.14-jdk21 AS build

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia os arquivos do projeto para dentro do container.
COPY . .

# Compila a aplicação e gera o arquivo JAR.
# --no-daemon evita iniciar um processo Gradle em segundo plano.
RUN gradle build --no-daemon


# Segunda etapa: utiliza uma imagem com Java 21 para executar a aplicação.
FROM eclipse-temurin:21-jdk

# Define o diretório de trabalho da aplicação.
WORKDIR /app

# Copia o JAR gerado na etapa de compilação para a imagem final.
COPY --from=build /app/build/libs/*.jar /app/usuario.jar

# Informa que a aplicação utiliza a porta 8080.
EXPOSE 8080

# Inicia a aplicação Java utilizando o arquivo JAR.
CMD ["java", "-jar", "/app/usuario.jar"]
