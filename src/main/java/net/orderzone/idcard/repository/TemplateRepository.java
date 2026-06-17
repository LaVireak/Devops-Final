package net.orderzone.idcard.repository;

import net.orderzone.idcard.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * TemplateRepository — used for listing, creating, searching, and checking
 * existence of {@link Template} records in the database.
 */
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    /** Find a template by its unique code (e.g. "DEFAULT", "GREEN"). */
    Optional<Template> findByCode(String code);

    /** Check whether a template code already exists. */
    boolean existsByCode(String code);

    /** Search templates by name (case-insensitive). */
    List<Template> findByNameContainingIgnoreCase(String name);
}
