package com.JpaRelationShips.onetomany.repository;

import com.JpaRelationShips.onetomany.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post,Long> {
}
