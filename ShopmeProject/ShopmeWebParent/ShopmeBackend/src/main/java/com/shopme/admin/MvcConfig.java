package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String userPhotosDirName = "user-photos";
		Path userPhotosDir = Paths.get(userPhotosDirName);
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + userPhotosDirName + "/**")
				.addResourceLocations("file:/" + userPhotosPath + "/");

		String catImgDirName = "../category-images";
		Path catImgDir = Paths.get(catImgDirName);
		String catImgPath = catImgDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/category-images/**").addResourceLocations("file:/" + catImgPath + "/");

		String brandLogoDirName = "../brand-logos";
		Path brandLogosDir = Paths.get(brandLogoDirName);
		String brandLogoPath = brandLogosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/" + brandLogoPath + "/");

	}

}
