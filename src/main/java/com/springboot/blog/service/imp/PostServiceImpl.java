package com.springboot.blog.service.imp;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFountException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository repo;
    private ModelMapper mapper;

    public PostServiceImpl(PostRepository repo, ModelMapper mapper) {
        this.mapper = mapper;
        this.repo = repo;
    }

    @Override
    public PostDto createPost(PostDto dto) {
        Post post = mapToEntity(dto);

        // create new post to database
        Post new_post = repo.save(post);

        PostDto post_response = mapToDto(new_post);

        return post_response;
    }

    @Override
    public PostResponse getAllPost(int page_no, int page_size, String sortBy, String sortDir) {

        // sort by ascending order
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create pagable instance
        PageRequest page = PageRequest.of(page_no, page_size, Sort.by(sortBy));
        Page<Post> posts = repo.findAll(page);

        List<Post> list_of_posts = posts.getContent();
        List<PostDto> content =  list_of_posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse post_response = new PostResponse();
        post_response.setContent(content);
        post_response.setPage_no(posts.getNumber());
        post_response.setPage_size(posts.getSize());
        post_response.setTotal_elems(posts.getTotalElements());
        post_response.setTotal_pages(posts.getTotalPages());
        post_response.setLast(posts.isLast());
        return post_response;
    }

    @Override
    public PostDto getPostById(Long id) {
        Post post = repo.findById(id).orElseThrow(()-> new ResourceNotFountException("post","id", id));
        return mapToDto(post);
    }

    @Override
    public PostDto updatePost(PostDto post_dto, long id) {
        Post post = repo.findById(id).orElseThrow(()-> new ResourceNotFountException("post","id", id));
        post.setTitle(post_dto.getTitle());
        post.setDescription(post_dto.getDescription());
        post.setContent(post_dto.getContent());

        Post updated_post = repo.save(post);
        return mapToDto(updated_post);
    }

    @Override
    public void deletePostbyId(long id) {
        Post post = repo.findById(id).orElseThrow(()-> new ResourceNotFountException("post","id", id));
        repo.delete(post);
    }

    // convert entity to DTO
    private PostDto mapToDto(Post new_post){
        // mapper approach
        PostDto post_dto = mapper.map(new_post, PostDto.class);
//        PostDto post_response = new PostDto();
//        post_response.setId(new_post.getId());
//        post_response.setTitle(new_post.getTitle());
//        post_response.setDescription(new_post.getDescription());
//        post_response.setContent(new_post.getContent());

        return post_dto;
    }

    // convert dto to entity
    private Post mapToEntity(PostDto dto){
        Post post = mapper.map(dto, Post.class);
//        Post post = new Post();
//        post.setTitle(dto.getTitle());
//        post.setDescription(dto.getDescription());
//        post.setContent(dto.getContent());
        return post;
    }
}
