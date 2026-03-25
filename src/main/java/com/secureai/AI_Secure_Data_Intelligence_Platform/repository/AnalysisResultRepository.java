package com.secureai.AI_Secure_Data_Intelligence_Platform.repository;

import com.secureai.AI_Secure_Data_Intelligence_Platform.model.AnalysisResult;

import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface AnalysisResultRepository
        extends MongoRepository<AnalysisResult, String> {

    List<AnalysisResult> findByInputType(String inputType);

    List<AnalysisResult> findByRiskLevel(String riskLevel);

    List<AnalysisResult> findByTimestampBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<AnalysisResult> findByRiskScoreGreaterThan(int score);

    List<AnalysisResult> findByAction(String action);

    List<AnalysisResult> findByOrderByTimestampDesc();

    List<AnalysisResult> findByInputTypeOrderByTimestampDesc(
            String inputType
    );

    long countByRiskLevel(String riskLevel);

    List<AnalysisResult> findTop10ByOrderByTimestampDesc();

    List<AnalysisResult> findByFileNameContaining(String fileName);

    @Query("{ 'findings.type': ?0 }")
    List<AnalysisResult> findByFindingType(String findingType);
}