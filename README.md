# Tu RUC Java Client

Cliente Java para consultar y buscar contribuyentes en la API de Tu RUC.

## Requisitos y compilación

La biblioteca funciona en Java 17, 21 y 25. La compilación usa JDK 25 por defecto
y genera bytecode Java 17; los consumidores no necesitan JDK 25.

```bash
./gradlew clean build
```

Para compilar y ejecutar los tests con otro JDK instalado:

```bash
./gradlew clean build -PjavaVersion=17
./gradlew clean build -PjavaVersion=21
```

El wrapper incluye Gradle 9.8.1 y verifica el SHA-256 de su distribución. Los
tests usan un servidor HTTP local con puerto efímero y no necesitan la API en
producción ni un servicio en `localhost:8080`. CI verifica Java 17, 21 y 25.

## Formato y análisis estático

[Spotless 8.10.4](https://plugins.gradle.org/plugin/com.diffplug.spotless/8.10.4)
aplica [Palantir Java Format 2.102.0](https://github.com/palantir/palantir-java-format)
a todas las fuentes Java, incluidos los tests, y formatea los archivos Gradle con
Groovy Eclipse. [PMD 7.28.0](https://pmd.github.io/2026/09/25/PMD-7.28.0/)
analiza producción y tests con las reglas de [config/pmd/ruleset.xml](config/pmd/ruleset.xml):
errores de igualdad, excepciones, recursos, código sin uso y operaciones redundantes.
El compilador también activa `-Xlint:all` y trata las advertencias como errores.

```bash
./gradlew spotlessApply            # Aplicar el formato
./gradlew spotlessCheck            # Comprobar el formato
./gradlew pmdMain pmdTest           # Analizar producción y tests
./gradlew check                    # Formato, análisis y tests
```

`build` incluye todas estas comprobaciones. Funcionan con los tres JDK admitidos;
se puede añadir `-PjavaVersion=17` o `-PjavaVersion=21` a los comandos anteriores.
CI ejecuta las mismas comprobaciones y falla si detecta un problema. Las herramientas
de calidad son dependencias del build y no se publican como dependencias del cliente.

## Uso desde otro proyecto Gradle

La versión de trabajo es `0.1.0`. Para consumir el checkout local, agrega a
`settings.gradle` del proyecto consumidor:

```groovy
includeBuild("../tu-ruc-java-client")
```

Y a su `build.gradle`:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ith:tu-ruc-client:0.1.0")
}
```

Gradle resuelve la biblioteca local y sus dependencias. El JAR generado en
`build/libs/` no incluye las dependencias dentro del archivo.
Este ejemplo con `includeBuild` compila el checkout y requiere JDK 25 instalado
con la configuración por defecto. Consumir el JAR ya compilado requiere Java 17
o superior.

```java
import org.ith.client.TuRuc;
import org.ith.client.TuRucClient;

TuRucClient client = new TuRuc("https://turuc.com.py/").getClient();
var response = client.getContribuyente("80024191-6").execute();

if (response.isSuccessful()) {
    var contribuyente = response.body().getData();
    System.out.println(contribuyente.getRuc()); // "80024191-6"
    System.out.println(contribuyente.getDoc()); // 80024191
} else {
    System.err.println(response.code() + ": " + response.errorBody().string());
}

var page = client.searchContribuyentes("NUÑEZ", 0).execute();
```

`execute()` puede lanzar `IOException` ante un fallo de transporte o un error
al convertir el JSON de la respuesta. Los errores HTTP se consultan mediante
`Response.isSuccessful()`, `code()` y `errorBody()`.
Las páginas comienzan en cero y la URL base debe terminar en `/`.

## Migración desde 0.0.1-beta

`0.1.0` cambia la API pública de `Contribuyente` para reflejar la respuesta actual
del servidor. Es necesario recompilar y adaptar a los consumidores:

| Antes | Ahora |
| --- | --- |
| `Integer getRuc()` | `String getRuc()` devuelve el RUC completo con DV |
| `setRuc(Integer)` | `setRuc(String)` recibe el RUC completo |
| Número de documento en `ruc` | `Integer getDoc()` y `setDoc(Integer)` |
| Constructor de tres argumentos | Constructor con los siete campos de la respuesta |

```java
var contribuyente = new org.ith.model.Contribuyente(
        80024191, "MINISTERIO DE PRUEBA", 6, "80024191-6", "ACTIVO", false, true);
```

También se exponen `estado`, `esPersonaJuridica` y `esEntidadPublica` mediante
getters y setters. `equals()` y `hashCode()` incluyen todos los campos del modelo.
`estado` puede ser `null`, tal como permite la API. El modelo usa `doc` y `ruc`
recibidos del servidor; no reconstruye uno a partir del otro.

La conversión JSON utiliza Fastjson2 en lugar de Gson. Los métodos públicos
siguen devolviendo `retrofit2.Call` con los mismos modelos. El conversor oficial
de Fastjson2 propaga los errores de conversión como `IOException`, con la causa
original; si capturabas excepciones específicas de Gson, adapta esas capturas.

## Dependencias

Retrofit usa 3.0.0; OkHttp y su interceptor de logs se alinean con el BOM 5.5.0.
La conversión JSON usa el módulo oficial `fastjson2-extension` 2.0.65, que incluye
el conversor para Retrofit y depende de `fastjson2` de la misma versión. Gson y
`converter-gson` dejan de formar parte de las dependencias de ejecución.
Los tests usan JUnit 6.1.3. Retrofit se declara como dependencia de API porque
los métodos públicos devuelven `retrofit2.Call`.
El BOM de OkHttp también se expone para alinear sus tipos transitivos entre la
compilación del consumidor y la ejecución.

El cliente procesa las respuestas JSON de texto de TuRuc mediante el conversor
[oficial de Fastjson2](https://github.com/alibaba/fastjson2/blob/2.0.65/extension/src/main/java/com/alibaba/fastjson2/support/retrofit/Retrofit2ConverterFactory.java).
La migración verifica compatibilidad funcional; no implica una mejora medida
del tiempo total de las consultas.

Consulta [CHANGELOG.md](CHANGELOG.md) para los cambios de esta versión.
