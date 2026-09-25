# JPA Repository

**Description**: Spring Data JPA interface extending `JpaRepository`. Add derived query methods and `@Query` for custom searches. Use for every entity requiring data access.

## Template
```java
public interface <Resource>Repository extends JpaRepository<<Resource>, Long> {

    // Derived finders
    List<<Resource>> findBy<Field>(Type value);
    List<<Resource>> findBy<Field>And<OtherField>(Type a, OtherType b);
    boolean existsBy<Field>(Type value);
    Optional<<Resource>> findBy<UniqueField>(Type value);

    // Custom JPQL query when derived names become unwieldy
    @Query("SELECT r FROM <Resource> r WHERE " +
           "(:param IS NULL OR r.field = :param) AND " +
           "r.availableCount >= :minCount")
    List<<Resource>> searchFlexible(
            @Param("param") Type param,
            @Param("minCount") int minCount);
}
```
