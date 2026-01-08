package com.JpaRelationShips.onetomany.repository;

import com.JpaRelationShips.onetomany.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepo extends JpaRepository<Comments, Long> {
}
