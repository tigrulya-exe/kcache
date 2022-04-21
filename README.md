# KCache
JVM library for server-side HTTP-cache validation boosting. KCache adds caching layer for [ETag](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/ETag) HTTP header in Spring Boot powered applications.

## Installation
[![](https://jitpack.io/v/tigrulya-exe/kcache.svg)](https://jitpack.io/#tigrulya-exe/kcache) <br>
[Maven/Gradle import instructions](https://jitpack.io/#tigrulya-exe/convoyeur/0.1.0) <br>

## Usage
### Mark request handler method as eligible for ETag caching
```kotlin
@GetMapping("/workers/{workerId}/area")
@KCacheable(
    resources = ["workers", "areas"],
    // optional
    keys = ["#args[0]"], 
    // optional
    resultBuilder = ResponseEntityKCacheResultBuilder::class 
)
fun getWorkerAreaById(
    @PathVariable workerId: Int
): ResponseEntity<Area> {
    return ResponseEntity.ok(
        // business logic
        workerService.getWorkerAreaById(workerId)
    )
}
```
### Mark method as cacheable resource state mutator
```kotlin
@KCacheEvict(
    resources = ["workers"],
    // optional
    keys = ["#args[0].id"], 
)
fun updateWorker(worker: Worker){
    workersRepository.update(worker)
}
```
### Configure framework
Framework configuration is available via Spring application.(yaml|properties), e.g.:
```yaml
kcache:
  jpa:
    listener:
      enable: true
  state-storage:
    # also supports RAM and redis storages    
    name: hazelcast
    hazelcast:
      discovery:
        type: TCP_IP
        tcp-ip:
          members: localhost
    redis:
      host: localhost
      port: 6379
  aop:
    # also supports AspectJ
    type: spring-aop
```


