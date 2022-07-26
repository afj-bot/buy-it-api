package com.afj.solution.buyitapp.model.product;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import com.afj.solution.buyitapp.model.User;
import com.afj.solution.buyitapp.model.category.Category;
import com.afj.solution.buyitapp.model.category.SubCategory;
import com.afj.solution.buyitapp.model.enums.Currency;

import static java.util.Objects.requireNonNull;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

/**
 * @author Tomash Gombosh
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1129189075285093051L;

    @Id
    @Type(type = "uuid-binary")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private float price;

    @Column(name = "description")
    private String description;

    @Column(name = "currency", columnDefinition = "ENUM('USD', 'UAH', 'EUR')")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @JsonManagedReference
    @OneToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn
    private Image image;

    @JsonManagedReference
    @OneToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn
    private Characteristic characteristic;

    @JsonManagedReference
    @ManyToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "created_user_id", nullable = false)
    private User user;

    @JsonManagedReference
    @OneToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonManagedReference
    @OneToOne(fetch = EAGER, cascade = ALL)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", fetch = EAGER, cascade = ALL)
    private Set<Rating> ratings = new HashSet<>();

    public Product(final Consumer<Product> builder) {
        requireNonNull(builder).accept(this);
    }

    @Override
    public String toString() {
        return String.format("{ \"id\": \"%s\", \"name\": \"%s\", \"description\": \"%s\", \"price\": \"%s\", "
                        + "\"currency\": \"%s\", \"image\": \"%s\", \"characteristic\": \"%s\", \"user\": \"%s\","
                        + " \"created_at\": \"%s\", \"updated_at\": \"%s\" }",
                this.getId(),
                this.getName(),
                this.getDescription(),
                this.getPrice(),
                this.getCurrency(),
                this.getImage(),
                this.getCharacteristic(),
                this.getUser(),
                this.getCreatedAt(),
                this.getUpdatedAt());
    }
}
