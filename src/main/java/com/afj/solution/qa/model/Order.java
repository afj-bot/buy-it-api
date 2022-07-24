package com.afj.solution.qa.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Tomash Gombosh
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_order")
@ToString
public class Order implements Serializable {

    @Id
    @Type(type = "uuid-binary")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Type(type = "uuid-binary")
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID user_id;

    @Type(type = "uuid-binary")
    @Column(name = "product_id", updatable = false, nullable = false)
    private UUID product_id;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
