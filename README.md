# Tu RUC Java Client

Cliente Java para consultar y buscar contribuyentes en la API de
[Tu RUC](https://turuc.com.py/).

## Requisitos

Java 17 o superior.

## Instalación

La biblioteca se publica en Maven Central como `py.com.ith:tu-ruc-client`.

Gradle:

```groovy
dependencies {
    implementation("py.com.ith:tu-ruc-client:0.1.0")
}
```

Maven:

```xml
<dependency>
    <groupId>py.com.ith</groupId>
    <artifactId>tu-ruc-client</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Uso

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

- La URL base debe terminar en `/`.
- Las páginas de búsqueda empiezan en 0.
- Los métodos devuelven `retrofit2.Call`: usa `execute()` para una llamada
  síncrona o `enqueue()` para una asíncrona.
- Crea el cliente una vez y reutilízalo; cada llamada a `getClient()` crea un
  cliente HTTP nuevo.

### Respuestas

`getContribuyente()` devuelve un `Contribuyente` dentro de `getData()`.
`searchContribuyentes()` devuelve un `SearchResponse` con `getContribuyentes()`
y `getPaginas()`.

| Campo de `Contribuyente` | Tipo      | Descripción                                 |
| ------------------------ | --------- | ------------------------------------------- |
| `getDoc()`               | `Integer` | Número de documento (CI)                    |
| `getRazonSocial()`       | `String`  | Nombre o razón social                       |
| `getDv()`                | `Integer` | Dígito verificador del RUC                  |
| `getRuc()`               | `String`  | RUC completo, con el dígito verificador     |
| `getEstado()`            | `String`  | Estado del contribuyente; puede ser `null`  |
| `getEsPersonaJuridica()` | `Boolean` | Indica si es persona jurídica               |
| `getEsEntidadPublica()`  | `Boolean` | Indica si es entidad pública                |

### Errores

- **Errores HTTP:** consúltalos con `response.isSuccessful()`, `code()` y
  `errorBody()`. No lanzan excepciones.
- **Fallos de red o JSON inválido:** `execute()` lanza `IOException`. Si el JSON
  no se puede convertir, la causa original viene en `getCause()`.

### Timeouts

Por defecto, todos los timeouts son de 60 segundos, incluido el de la llamada
completa. Para cambiarlos, indica los valores en segundos:

```java
// callTimeout, writeTimeout, readTimeout, connectTimeout
TuRucClient client = new TuRuc("https://turuc.com.py/", 30, 10, 10, 5).getClient();
```

### Logging

Por defecto, el cliente no registra el tráfico HTTP. Para depurar, indica un
nivel de `HttpLoggingInterceptor` de OkHttp:

```java
import okhttp3.logging.HttpLoggingInterceptor;

TuRucClient client = new TuRuc("https://turuc.com.py/")
        .withLogLevel(HttpLoggingInterceptor.Level.BODY)
        .getClient();
```

`BASIC` registra la petición y el estado de la respuesta; `HEADERS` agrega las
cabeceras; `BODY` también registra los cuerpos, con los datos de los
contribuyentes consultados. Los mensajes van al log de OkHttp
(`java.util.logging`, logger `okhttp3.OkHttpClient`).

## Migración desde 0.0.1-beta

La versión 0.1.0 cambia el modelo `Contribuyente` para reflejar la respuesta
actual de la API. Recompila tu proyecto y adapta estos usos:

| Antes                          | Ahora                                                     |
| ------------------------------ | --------------------------------------------------------- |
| `Integer getRuc()`             | `String getRuc()`, con el dígito verificador              |
| `setRuc(Integer)`              | `setRuc(String)`, con el RUC completo                     |
| Número de documento en `ruc`   | `Integer getDoc()` y `setDoc(Integer)`                    |
| Constructor de tres argumentos | Constructor con los siete campos, en el orden de la tabla |

```java
var contribuyente = new org.ith.model.Contribuyente(
        80024191, "MINISTERIO DE PRUEBA", 6, "80024191-6", "ACTIVO", false, true);
```

`equals()` y `hashCode()` ahora comparan los siete campos.

El JSON se convierte con Fastjson2 en lugar de Gson. Si capturabas excepciones
de Gson, captura `IOException`. La biblioteca ya no trae Gson como dependencia;
si tu proyecto lo usa, decláralo por tu cuenta.

Las dependencias transitivas pasan a Retrofit 3.0.0 y OkHttp 5.5.0.

Las coordenadas Maven pasan de `org.ith:tu-ruc-client` a
`py.com.ith:tu-ruc-client`. Los paquetes Java no cambian: los imports siguen
siendo `org.ith.client` y `org.ith.model`.

El cliente ya no registra las peticiones y respuestas completas. Si dependías de
esos logs, actívalos con `withLogLevel(HttpLoggingInterceptor.Level.BODY)`.

## Cambios

Consulta [CHANGELOG.md](CHANGELOG.md) para conocer los cambios de cada versión.

## Licencia

[MIT](LICENSE).

## Contribuciones

Para compilar, ejecutar las pruebas y conocer las comprobaciones de calidad,
consulta [CONTRIBUTING.md](CONTRIBUTING.md).
