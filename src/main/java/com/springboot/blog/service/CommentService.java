package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long post_id, CommentDto dto);
    List<CommentDto> getCommentsByPostID(long post_id);
    CommentDto getCommentsByID(Long post_id, long comment_id);
    CommentDto updateComment(Long post_id, Long comment_id, CommentDto comment_request);
    void deleteComment(long post_id, long comment_id);
}
