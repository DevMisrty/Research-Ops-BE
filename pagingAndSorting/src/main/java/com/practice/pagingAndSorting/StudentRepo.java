package com.practice.pagingAndSorting;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface StudentRepo extends ListPagingAndSortingRepository<Student,Integer> {

    List<Student> findByOrderByFirstName();
    List<Student> findByOrderByLastNameDesc();
}
