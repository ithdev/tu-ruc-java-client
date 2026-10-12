# Changelog

## 0.1.0 — Sin publicar

- Publicación en Maven Central como `py.com.ith:tu-ruc-client`, con JAR de
  fuentes, Javadoc y firmas PGP. El `groupId` cambia de `org.ith` a `py.com.ith`,
  el dominio de ITH; los paquetes Java `org.ith.*` no cambian.
- Licencia MIT.
- Compatibilidad verificada con Java 17, 21 y 25; JDK 25 para compilar por defecto
  y bytecode Java 17 para consumidores.
- Gradle 9.8.1 con wrapper regenerado y SHA-256 de distribución verificado.
- Spotless 8.10.4 con Palantir Java Format 2.102.0 para todas las fuentes y tests;
  formato de los scripts Gradle con Groovy Eclipse.
- PMD 7.28.0 para analizar producción y tests, más advertencias de `javac`
  tratadas como errores. `check`, `build` y CI incluyen las comprobaciones de calidad.
- Retrofit 3.0.0, OkHttp/logging-interceptor 5.5.0 mediante BOM y JUnit 6.1.3.
  Se elimina `okhttp-urlconnection`, que no se utiliza.
- Fastjson2 2.0.65 y su conversor oficial de Retrofit reemplazan a Gson y
  `converter-gson`. Se conservan los métodos públicos y los modelos. Los errores
  de conversión JSON se propagan como `IOException` con la causa original.
- Cambio incompatible: `Contribuyente.getRuc()` devuelve `String` con el DV y
  `setRuc()` recibe `String`. El documento numérico se obtiene con `getDoc()`.
  El constructor completo recibe `doc`, `razonSocial`, `dv`, `ruc`, `estado`,
  `esPersonaJuridica` y `esEntidadPublica`; el constructor vacío continúa disponible.
- `equals()` y `hashCode()` consideran los siete campos de la respuesta.
- Retrofit se expone como dependencia de API para que los consumidores puedan
  compilar contra los tipos públicos del cliente.
- Pruebas HTTP locales de consulta, búsqueda, campos adicionales/nulos, errores
  HTTP y JSON inválido; matriz CI Java 17/21/25.
