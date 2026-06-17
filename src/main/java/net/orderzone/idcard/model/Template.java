package net.orderzone.idcard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 120)
    private String organizationName;

    @Column(nullable = false, length = 20)
    private String layout = "VERTICAL";

    @Column(nullable = false, length = 7)
    private String primaryColor = "#1d4ed8";

    @Column(nullable = false, length = 7)
    private String secondaryColor = "#e0e7ff";

    @Column(nullable = false, length = 7)
    private String textColor = "#111827";

    @Column(length = 255)
    private String tagline;

    // ── Constructors ──────────────────────────────────────────────────────────

    public Template() {}

    private Template(Builder b) {
        this.code = b.code;
        this.name = b.name;
        this.organizationName = b.organizationName;
        this.layout = b.layout != null ? b.layout : "VERTICAL";
        this.primaryColor = b.primaryColor != null ? b.primaryColor : "#1d4ed8";
        this.secondaryColor = b.secondaryColor != null ? b.secondaryColor : "#e0e7ff";
        this.textColor = b.textColor != null ? b.textColor : "#111827";
        this.tagline = b.tagline;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getLayout() { return layout; }
    public void setLayout(String layout) { this.layout = layout; }

    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }

    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }

    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }

    public String getTagline() { return tagline; }
    public void setTagline(String tagline) { this.tagline = tagline; }

    // ── Builder ────────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String code, name, organizationName, layout, primaryColor, secondaryColor, textColor, tagline;

        public Builder code(String code) { this.code = code; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder organizationName(String organizationName) { this.organizationName = organizationName; return this; }
        public Builder layout(String layout) { this.layout = layout; return this; }
        public Builder primaryColor(String primaryColor) { this.primaryColor = primaryColor; return this; }
        public Builder secondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; return this; }
        public Builder textColor(String textColor) { this.textColor = textColor; return this; }
        public Builder tagline(String tagline) { this.tagline = tagline; return this; }
        public Template build() { return new Template(this); }
    }
}
