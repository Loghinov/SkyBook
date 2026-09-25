# JPA Entity

**Description**: Standard entity class with Lombok, auto-generated id, and `@PrePersist` timestamp. Use for every new database table.

## Template
```java
@Entity
@Table(name = "<table_name>s")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class <Resource> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String <field>;

    // Enum column
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private <StatusEnum> status;

    // ManyToOne relationship (lazy by default)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "<fk>_id", nullable = false)
    private <Other> <other>;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
```
