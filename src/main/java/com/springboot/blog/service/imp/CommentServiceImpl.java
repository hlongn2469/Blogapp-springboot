package com.springboot.blog.service.imp;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFountException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository comment_repo;
    private PostRepository post_repo;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository comment_repo, PostRepository post_repo, ModelMapper mapper) {
        this.comment_repo = comment_repo;
        this.post_repo = post_repo;
        this.mapper = mapper;
    }


    @Override
    public CommentDto createComment(long post_id, CommentDto dto) {
        Comment new_comment = mapToEntity(dto);

        // retrieve post entity by ID
        Post post = post_repo.findById(post_id).orElseThrow(()-> new ResourceNotFountException("Post","id",post_id));

        // set post to comment entity
        new_comment.setPost(post);

        // set comment entity to DB
        Comment db_comment = comment_repo.save(new_comment);

        return mapToDto(db_comment);
    }

    @Override
    public List<CommentDto> getCommentsByPostID(long post_id) {
        // retrieve comments by post id
        List<Comment> comments = comment_repo.findByPostId(post_id);

        // convert list of comment entities to list of comments dto
        return comments.stream().map(comment ->mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentsByID(Long post_id, long comment_id) {
        // retrieve post entity by ID
        Post post = post_repo.findById(post_id).orElseThrow(()-> new ResourceNotFountException("Post","id",post_id));

        // retrieve comment by comment id
        Comment comment = comment_repo.findById(comment_id).orElseThrow(()-> new ResourceNotFountException("Comment", "id", comment_id));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDto(comment);
    }

    @Override
    public CommentDto updateComment(Long post_id, Long comment_id, CommentDto comment_request) {
        // retrieve post entity by ID
        Post post = post_repo.findById(post_id).orElseThrow(()-> new ResourceNotFountException("Post","id",post_id));

        Comment comment = comment_repo.findById(comment_id).orElseThrow(()-> new ResourceNotFountException("Comment","id",comment_id));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment.setName(comment_request.getName());
        comment.setEmail(comment_request.getEmail());
        comment.setBody(comment_request.getBody());
        Comment updated_comment = comment_repo.save(comment);
        return mapToDto(updated_comment);
    }

    @Override
    public void deleteComment(long post_id, long comment_id) {
        // retrieve post entity by ID
        Post post = post_repo.findById(post_id).orElseThrow(()-> new ResourceNotFountException("Post","id",post_id));

        // retrieve comment by comment id
        Comment comment = comment_repo.findById(comment_id).orElseThrow(()-> new ResourceNotFountException("Comment", "id", comment_id));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        comment_repo.delete(comment);
    }

    // convert entity to dto
    private CommentDto mapToDto(Comment new_comment){
//        CommentDto new_dto = new CommentDto();
//        new_dto.setId(new_comment.getId());
//        new_dto.setName(new_comment.getName());
//        new_dto.setEmail(new_comment.getEmail());
//        new_dto.setBody((new_comment.getBody()));
        CommentDto new_dto = mapper.map(new_comment, CommentDto.class);

        return new_dto;
    }

    // convert dto to entity
    private Comment mapToEntity(CommentDto dto){
//        Comment new_comment = new Comment();
//        new_comment.setId(dto.getId());
//        new_comment.setBody(dto.getBody());
//        new_comment.setEmail(dto.getEmail());
//        new_comment.setName(dto.getName());
        Comment new_comment = mapper.map(dto, Comment.class);
        return new_comment;
    }
}
