package com.JpaRelationShips.onetomany;

import com.JpaRelationShips.onetomany.model.Comments;
import com.JpaRelationShips.onetomany.model.Post;
import com.JpaRelationShips.onetomany.repository.CommentsRepo;
import com.JpaRelationShips.onetomany.repository.PostRepo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {

    private PostRepo postRepo;
    private CommentsRepo commentsRepo;

    @PostMapping("/post")
    public Post addPost(@RequestBody Post post){
        return postRepo.save(post);
    }

    @PostMapping("/comments")
    public Comments addComments(@RequestBody Comments comments){
        return commentsRepo.save(comments);
    }
}
