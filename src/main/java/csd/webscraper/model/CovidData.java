package csd.webscraper.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CovidData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "new_cases")
    @NotNull(message = "New cases count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int newCases;

    @Column(name = "new_community_cases")
    @NotNull(message = "New community cases count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int newCommunityCases;

    @Column(name = "new_dormitory_cases")
    @NotNull(message = "New dormitory cases count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int newDormitoryCases;

    @Column(name = "new_imported_cases")
    @NotNull(message = "New imported cases count count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int newImportedCases;

    @Column(name = "total_deaths")
    @NotNull(message = "Death count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalDeaths;

    @Column(name = "total_swabs")
    @NotNull(message = "Total swab count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalSwab;

    @Column(name = "total_vaccination_dose")
    @NotNull(message = "Total vaccination dose count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalVaccinationDoses;

    @Column(name = "total_at_least_one_dose")
    @NotNull(message = "Number of people who received at least one dose must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalAtLeastOneDose;

    @Column(name = "total_completed_full_regimen")
    @NotNull(message = "Number of people who completed full regimen must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalCompletedFullRegimen;

    @Column(name = "total_covid_cases")
    @NotNull(message = "Number of covid cases must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalCovidCases;

    @Column(name = "total_recovered")
    @NotNull(message = "Number of recovered must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalRecovered;

    @Column(name = "total_population")
    @NotNull(message = "Number of total population must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalPopulation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
