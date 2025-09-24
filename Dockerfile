# アプリケーションの構築
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /workspace

# Gradleラッパーとビルドファイルをコピーする
COPY gradlew .
COPY gradlew.bat .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# gradlewを実行可能にする
RUN chmod +x gradlew

# ソースコードをコピーする
COPY src ./src

# アプリケーションをビルドし、実行可能JARファイルを作成する
RUN ./gradlew bootJar

# 最終的な、より小さなイメージを作成する
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# ビルダーステージから実行可能JARをコピーする
COPY --from=builder /workspace/build/libs/*.jar app.jar

# アプリが動作するポートを公開する
EXPOSE 8080

# アプリケーションを実行するコマンド
ENTRYPOINT ["java", "-jar", "app.jar"]
