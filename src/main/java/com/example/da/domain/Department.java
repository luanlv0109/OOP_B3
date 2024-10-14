    package com.example.da.domain;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Table;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;

    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "departments")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    public class Department {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "code", nullable = false, length = 50)
        private String code;

        @Column(name = "name", nullable = false, length = 50)
        private String name;
    }
