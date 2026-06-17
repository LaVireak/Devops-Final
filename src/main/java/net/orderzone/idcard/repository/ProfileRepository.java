package net.orderzone.idcard.repository;

import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ProfileRepository — used for listing, creating, searching, and checking
 * existence of {@link Profile} records in the database.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /** Find all profiles of a given type. */
    List<Profile> findByType(ProfileType type);

    /** Count profiles of a given type. */
    long countByType(ProfileType type);

    /** Look up a profile by its public UUID (used in QR code verification). */
    Optional<Profile> findByUuid(String uuid);

    /** Look up a profile by its human-readable registration number. */
    Optional<Profile> findByRegistrationNumber(String registrationNumber);

    /** Check whether a registration number is already taken. */
    boolean existsByRegistrationNumber(String registrationNumber);

    /** Full-name contains search (case-insensitive), paginated. */
    Page<Profile> findByFullNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Count how many profiles already have a registration number that starts
     * with {@code prefix} (e.g. "2026-ENG-") so we can compute the next seq.
     */
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.registrationNumber LIKE :prefix%")
    long countByRegistrationNumberPrefix(@Param("prefix") String prefix);
}
