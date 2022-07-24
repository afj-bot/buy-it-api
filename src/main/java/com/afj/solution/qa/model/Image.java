package com.afj.solution.qa.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.function.Consumer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import static java.util.Objects.requireNonNull;


/**
 * @author Tommash Gombosh
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image")
@ToString
public class Image implements Serializable {

    private static final long serialVersionUID = -3971169410727710315L;

    @Id
    @Type(type = "uuid-binary")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @JsonBackReference
    @OneToOne(mappedBy = "image")
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

}
