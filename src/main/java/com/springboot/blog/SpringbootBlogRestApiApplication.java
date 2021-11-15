package com.springboot.blog;

import com.springboot.blog.entity.Role;
import com.springboot.blog.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringbootBlogRestApiApplication implements CommandLineRunner {
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	public static  void main(String[] args) {
		SpringApplication.run(SpringbootBlogRestApiApplication.class, args);
	}

	@Autowired
	private RoleRepository role_repo;

	@Override
	public void run(String... args) throws Exception {
		Role admin_role = new Role();
		admin_role.setName("ROLE_ADMIN");
		role_repo.save(admin_role);

		Role user_role = new Role();
		user_role.setName("ROLE_USER");
		role_repo.save(user_role);
	}
}
