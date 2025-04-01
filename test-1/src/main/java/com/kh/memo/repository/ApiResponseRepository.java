package com.kh.memo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.memo.entity.ApiResponseEntity;

import java.util.List;

public interface ApiResponseRepository extends JpaRepository<ApiResponseEntity, Long>{
    List<ApiResponseEntity> findByDataContaining(String keyword);
    List<ApiResponseEntity> findByShowDate(String showDate);
}
