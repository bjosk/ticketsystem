package com.ticketsystem.ticketsystem.Repository;

import com.ticketsystem.ticketsystem.Model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
