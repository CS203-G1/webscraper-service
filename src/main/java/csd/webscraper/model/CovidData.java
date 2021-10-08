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

    @Column(name = "new_local_cases")
    @NotNull(message = "New local cases count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int newLocalCases;

    @Column(name = "new_imported_cases")
    @NotNull(message = "New imported cases count count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int newImportedCases;

    @Column(name = "hospitalised")
    @NotNull(message = "Hospitalised count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int hospitalised;

    @Column(name = "hospitalised_icu")
    @NotNull(message = "Hospitalised ICU count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int hospitalisedICU;

    @Column(name = "require_oxygen_supplementation")
    @NotNull(message = "Require Oxygen Supplementation count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int requireOxygen;

    @Column(name = "total_deaths")
    @NotNull(message = "Death count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalDeaths;

    @Column(name = "total_swabs")
    @NotNull(message = "Total swab count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalSwab;

    @Column(name = "average_daily_swab_per_week")
    @NotNull(message = "Average daily number of swab count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int averageDailySwabPerWeek;

    @Column(name = "total_swabs_per_million")
    @NotNull(message = "Total swabs per 1,000,000 in total population must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalSwabPerMillion;

    @Column(name = "total_vaccination_dose")
    @NotNull(message = "Total vaccination dose count must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalVaccinationDoses;

    @Column(name = "at_least_one_dose")
    @NotNull(message = "Number of people who received at least one dose must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int atLeastOneDose;

    @Column(name = "completed_full_regimen")
    @NotNull(message = "Number of people who completed full regimen must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int completedFullRegimen;

    @Column(name = "total_covid_cases")
    @NotNull(message = "Number of covid cases must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalCovidCases;

    @Column(name = "total_recovered")
    @NotNull(message = "Number of recovered must not be null")
    @PositiveOrZero(message = "Count must be positive or zero")
    private int totalRecovered;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
