package com.example.da.repository;

import com.example.da.domain.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

    Optional<SearchKeyword> findByKeywordAndDesiredSuggestion(String keyword, String desiredSuggestion);
    @Query("SELECT sk FROM SearchKeyword sk WHERE sk.timesPerMonth = :month AND FUNCTION('YEAR', sk.createdAt) = :year")
    List<SearchKeyword> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

}
