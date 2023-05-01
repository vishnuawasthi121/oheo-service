package com.ogive.oheo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.dto.ProductRequestDTO;

public class ApiTestClient {

	public static void test1() throws IOException {
		ProductRequestDTO product = new ProductRequestDTO();

		product.setEngine("Engine");
		//product.setImageType("Slider");

		List<MultipartFile> images = new ArrayList();
		File file = new File("C:\\Users\\Lenovo\\Downloads\\Right hand.jpg");

		FileInputStream inputStream1 = new FileInputStream(file);
		FileInputStream inputStream2 = new FileInputStream(file);

		MockMultipartFile mockMultipartFile1 = new MockMultipartFile("File1.jpg", inputStream1);
		MockMultipartFile mockMultipartFile2 = new MockMultipartFile("File2.jpg", inputStream2);

		images.add(mockMultipartFile1);
		images.add(mockMultipartFile2);

	//	product.setImages(images);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

		messageConverters.add(new ByteArrayHttpMessageConverter());
		messageConverters.add(new StringHttpMessageConverter());
		messageConverters.add(new ResourceHttpMessageConverter(false));
		// messageConverters.add( new FileDescriptorSerializer());

		RestTemplate restTemplate = new RestTemplate(messageConverters);

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.MULTIPART_FORM_DATA));
		HttpEntity<ProductRequestDTO> entity = new HttpEntity<ProductRequestDTO>(product, headers);

		String body = restTemplate
				.exchange("http://localhost:8081/oheo-service/cms/v2/products", HttpMethod.POST, entity, String.class)
				.getBody();

		System.out.println("@@@@@@@@@@@@ body   : " + body);
	}
	public static void main(String[] args) throws IOException {

		String name = ImageType.Slider.getValue();
		System.out.println(name);
		
	}

}
