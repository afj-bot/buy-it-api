package com.afj.solution.buyitapp.model.product;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import static java.util.Objects.requireNonNull;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;


/**
 * @author Tommash Gombosh
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image")
public class Image implements Serializable {

    private static final long serialVersionUID = -3971169410727710315L;

    @Id
    @Type(type = "uuid-binary")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @JsonBackReference
    @OneToOne(fetch = EAGER, cascade = ALL, mappedBy = "image")
    private Product product;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    public Image(final Consumer<Image> builder) {
        requireNonNull(builder).accept(this);
    }

    @Override
    public String toString() {
        return String.format("{ \"id\": \"%s\", \"fileName\": \"%s\", \"createdAt\": \"%s\", \"updated_at\": \"%s\" }",
                this.getId(),
                this.getFileName(),
                this.getCreatedAt(),
                this.getUpdatedAt());
    }

}
