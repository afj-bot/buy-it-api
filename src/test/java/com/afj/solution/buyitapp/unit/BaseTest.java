package com.afj.solution.buyitapp.unit;

import java.util.Set;
import java.util.UUID;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import com.afj.solution.buyitapp.model.User;
import com.afj.solution.buyitapp.model.category.Category;
import com.afj.solution.buyitapp.repository.CategoryRepository;
import com.afj.solution.buyitapp.service.user.UserService;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

/**
 * @author Tomash Gombosh
 */
@TestInstance(PER_CLASS)
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest implements WithAssertions {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryRepository categoryRepository;

    protected final UUID productId = UUID.fromString("2a51b256-a6ef-4748-9354-a869290c3bf0");
    protected final UUID userId = UUID.fromString("3b0a4223-35e6-47b1-9ac3-f95911979574");
    protected final UUID cancelOrderId = UUID.fromString("3e5451f2-efb3-42ce-88f1-1a566421e676");
    protected final UUID waitingForPaymentOrderId = UUID.fromString("6434e8d9-3ab1-40e0-88c2-bc318538f8e1");
    protected final UUID inProgressOrderId = UUID.fromString("78452df2-1e97-4c0e-a691-9b381c714850");

    protected UUID categoryId;
    protected UUID subCategoryId;

    @BeforeAll
    void unitTestSetup() {
        final User user = userService.findById(UUID.fromString("3b0a4223-35e6-47b1-9ac3-f95911979574"));
        user.setAuthorities(Set.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")));
        userService.updateUser(user.getId(), user);

        final Category testCategory = categoryRepository.findAll()
                .stream()
                .filter(c -> "cars".equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No categories for tests"));

        categoryId = testCategory.getId();
        subCategoryId = testCategory.getSubCategories()
                .stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("No categories for tests"))
                .getId();
    }
}
