# Contribuir

## Compilación

La compilación usa JDK 25 por defecto y genera bytecode Java 17.

```bash
./gradlew clean build
```

Para compilar y ejecutar los tests con otro JDK instalado:

```bash
./gradlew clean build -PjavaVersion=17
./gradlew clean build -PjavaVersion=21
```

El wrapper incluye Gradle 9.8.1 y verifica el SHA-256 de su distribución. Los
tests usan un servidor HTTP local con puerto efímero; no necesitan la API en
producción ni un servicio en `localhost:8080`.

## Formato y análisis estático

- [Spotless](https://plugins.gradle.org/plugin/com.diffplug.spotless) aplica
  [Palantir Java Format](https://github.com/palantir/palantir-java-format) a
  todas las fuentes Java, incluidos los tests, y formatea los archivos Gradle con
  Groovy Eclipse.
- [PMD](https://pmd.github.io/) analiza producción y tests con las reglas de
  [config/pmd/ruleset.xml](config/pmd/ruleset.xml): errores de igualdad,
  excepciones, recursos, código sin uso y operaciones redundantes.
- El compilador activa `-Xlint:all` y trata las advertencias como errores.

```bash
./gradlew spotlessApply            # Aplicar el formato
./gradlew spotlessCheck            # Comprobar el formato
./gradlew pmdMain pmdTest          # Analizar producción y tests
./gradlew check                    # Formato, análisis y tests
```

`build` incluye todas estas comprobaciones. Funcionan con los tres JDK admitidos;
puedes añadir `-PjavaVersion=17` o `-PjavaVersion=21` a los comandos anteriores.
Las herramientas de calidad son dependencias del build y no se publican como
dependencias del cliente.

## Integración continua

CI ejecuta `./gradlew clean build` con Java 17, 21 y 25 y falla si alguna
comprobación detecta un problema.

## Dependencias

Retrofit se declara como dependencia `api` porque los métodos públicos devuelven
`retrofit2.Call`. El BOM de OkHttp también se expone para alinear sus tipos
transitivos entre la compilación del consumidor y la ejecución. La conversión
JSON usa el módulo oficial `fastjson2-extension`, que incluye el
[conversor para Retrofit](https://github.com/alibaba/fastjson2/blob/2.0.65/extension/src/main/java/com/alibaba/fastjson2/support/retrofit/Retrofit2ConverterFactory.java).

Registra los cambios de cada versión en [CHANGELOG.md](CHANGELOG.md).
