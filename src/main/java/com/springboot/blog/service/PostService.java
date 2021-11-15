package com.springboot.blog.service;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto dto);
    PostResponse getAllPost(int page_num, int page_size, String sortBy, String sortDir);
    PostDto getPostById(Long id);
    PostDto updatePost(PostDto post_dto, long id);
    void deletePostbyId(long id);
}
