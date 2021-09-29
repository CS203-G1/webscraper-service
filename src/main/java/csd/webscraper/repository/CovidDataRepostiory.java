package csd.webscraper.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import csd.webscraper.model.CovidData;

@Repository
public interface CovidDataRepostiory extends JpaRepository<CovidData, UUID> {

}
