package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    private CommentService comment_service;

    public CommentController(CommentService comment_service) {
        this.comment_service = comment_service;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value = "postId") long id,
                                                    @Valid @RequestBody CommentDto dto){
        return new ResponseEntity<>(comment_service.createComment(id,dto), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value = "postId") long post_id){
        return comment_service.getCommentsByPostID(post_id);
    }

    @GetMapping("/posts/{postId}/comments/{commentID}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value = "postId") long post_id,
                                                     @PathVariable(value = "commentID") long comment_id){
        CommentDto dto = comment_service.getCommentsByID(post_id,comment_id);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @PutMapping("/posts/{postId}/comments/{commentID}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") long post_id,
                                                    @PathVariable(value = "commentID") long comment_id,
                                                    @Valid @RequestBody CommentDto dto){
        CommentDto update_comment = comment_service.updateComment(post_id, comment_id, dto);
        return new ResponseEntity<>(update_comment,HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postId}/comments/{commentID}")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") long post_id,
                                                 @PathVariable(value = "commentID") long comment_id){
        comment_service.deleteComment(post_id,comment_id);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }


}
