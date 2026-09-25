# REST Controller

**Description**: Standard REST endpoint class. Use for every new resource exposed via HTTP.

## Template
```java
@RestController
@RequestMapping("/api/<resource>s")
@RequiredArgsConstructor
public class <Resource>Controller {

    private final <Resource>Service <resource>Service;
    // add SecurityUtils if ownership checks are needed

    @GetMapping
    public ResponseEntity<List<<Resource>Response>> getAll() {
        return ResponseEntity.ok(<resource>Service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<<Resource>Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(<resource>Service.getById(id));
    }

    @PostMapping
    public ResponseEntity<<Resource>Response> create(
            @Valid @RequestBody Create<Resource>Request request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(<resource>Service.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        <resource>Service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```