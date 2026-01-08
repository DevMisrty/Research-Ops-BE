package com.OneToOne.RelationalMapping.Repo;

import com.OneToOne.RelationalMapping.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message,Integer> {
}
