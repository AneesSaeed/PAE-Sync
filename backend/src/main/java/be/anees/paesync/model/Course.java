package be.anees.paesync.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "courses") // tells Spring Data MongoDB that this Java class (Course) corresponds to a MongoDB collection named "courses".
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @NotBlank(message = "Le sigle ne peut pas être vide.")
    @Pattern(
        regexp = "^[1-5][A-Za-z]{3}[0-9]$",
        message = "Le sigle doit commencer par un chiffre entre 1 et 5, suivi de trois lettres et d'un chiffre (ex: 3DEV3)."
    )
    private String acronym;

    @NotBlank(message = "Le titre ne peut pas être null.")
    private String title;

    @Min(value = 1, message = "Les crédits doivent être au moins 1")
    @Max(value = 6, message = "Les crédits ne doivent pas dépasser 6")
    private int ects;

    @Min(value = 1, message = "Le bloc doit être au moins 1")
    @Max(value = 3, message = "Le bloc ne doit pas dépasser 3")
    private int block;

    private String professor;

    
    // --- Custom setters to enforce formatting ---
    public void setAcronym(String acronym) {
        this.acronym = acronym != null ? acronym.toUpperCase() : null;
    }

    public void setProfessor(String professor) {
        this.professor = professor != null ? professor.toUpperCase() : null;
    }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            this.title = title;
        } else {
            String trimmed = title.trim().toLowerCase();
            this.title = Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1);
        }
    }
}
