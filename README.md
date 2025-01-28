# What is Caching? 

Caching is a mechanism to enhance the performance of a system. 
It is a temporary memory that lies between the application and the persistent database. Cache memory stores recently used data items in order to reduce the number of database hits as much as possible

# Spring starter cache dependency
Include the latest version of spring-boot-starter-cache dependency that transitively includes spring-context-support and spring-context modules. Both context modules provide the necessary classes and interfaces for handling the caching feature.

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

# Cache Provider

Spring boot needs an underlying cache provider that can store and manage the cached objects and support lookups. Spring boot autoconfigures one of these providers with default options if it is present in the classpath and we have enabled cache by @EnableCaching.

- JCache (JSR-107) (EhCache 3, Hazelcast, Infinispan, and others)
- EhCache
- Hazelcast
- Infinispan
- Couchbase
- Redis
- Caffeine
- Simple cache

To customize the configuration of any of the above providers, we can place the corresponding configuration file in the /resources folder and supply the config location to Spring boot.

# Spring Caching Annotations

- @Cacheable: Triggers cache population.
- @CacheEvict: Triggers cache eviction.
- @CachePut: Updates the cache without interfering with the method execution.
- @Caching: Regroups multiple cache operations to be applied on a method.
- @CacheConfig: Shares some standard cache-related settings at the class level.

# @Cacheable
It is used on the method level to let spring know that the response of the method is cacheable. Spring intercepts the request/response of this method and stores the response in the cache by the name specified in the annotation attribute e.g. @Cacheable(“employees”).

```
@Cacheable("employees")
public Optional<Employee> getEmployeeById(Long id) {
return repository.findById(id);
}
```
Since caches are essentially key-value stores, each invocation of a cached method needs to be translated into a suitable key for cache access. By default, Spring uses the method parameters to form the cache keys as follows:

- If no params are given, return SimpleKey.EMPTY.
- If only one param is given, return that instance.
- If more than one param is given, return a SimpleKey that contains all parameters.

- It is very necessary to understand that keys must implement valid hashCode() and equals() methods contract for correct lookups.

```
@Cacheable(value = "employees", key = "#id")
public Optional<Employee> getEmployeeById(Long id) {...}

@Cacheable(value = "employees", key = "#department.id")
public List<Employee> getEmployeesByDepartmentId(Department department) {...}
```
We can also do the caching only when a certain condition is satisfied. In the following example, we are caching when the employee id is greater than 0;

```
@Cacheable(value = "employees", key = "#id", condition="#id > 0")
public Optional<Employee> getEmployeeById(Long id) {...}
```

# @CachePut
The @CachePut annotation is very similar to the @Cacheable annotation except the annotated method is always executed irrespective of whether the cache key is present in the cache or not. It supports the same options as @Cacheable.

```
@CachePut(cacheNames = "employees", key = "#employee.id")
public Employee updateEmployee(Employee employee) {...}
```


# @CacheEvict
This annotation is helpful in evicting (removing) the cache previously loaded. When @CacheEvict annotated methods will be executed, it will clear the cache matched with a cache name, cache key or a condition to be specified.
```
@CacheEvict(cacheNames="employees", key="#id")
public void deleteEmployee(Long id) {...}
```

The @CacheEvict annotation offers an extra parameter ‘allEntries’ for evicting the whole specified cache, rather than a key in the cache.

```
@CacheEvict(cacheNames="employees", allEntries=true)
public void deleteAllEmployees() {...}
```

# @Caching
The @Caching annotation is needed to group multiple annotations when we need to use multiple cache annotations in a single method. In the following example, we are using the @CacheEvict annotation, twice.

```
@Caching(evict = {
@CacheEvict(cacheNames = "departments", allEntries = true),
@CacheEvict(cacheNames = "employees", key = "...")})
public boolean importEmployees(List<Employee> data) { ... }

```

# @CacheConfig
This annotation allows us to specify some of the cache configurations at the class level, so we do not have to repeat them multiple times over each method.

```
@Service
@CacheConfig(cacheNames={"employees"})
public class EmployeeService {

    @Autowired
    EmployeeRepository repository;
    
    @Cacheable(key = "#id")
    public Optional<Employee> getEmployeeById(Long id) { ... }
    
    @CachePut(key = "#employee.id")
    public Employee updateEmployee(Employee employee) { ... }
    
}
 ```
