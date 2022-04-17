package com.bluebug.gamehub.gamehub_s3_api.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bluebug.gamehub.gamehub_core.dto.ErrorDto;
import com.bluebug.gamehub.gamehub_core.dto.FieldErrorDto;
import com.bluebug.gamehub.gamehub_s3_api.dto.DeleteResultDto;
import com.bluebug.gamehub.gamehub_s3_api.dto.UploadDto;
import com.bluebug.gamehub.gamehub_s3_api.dto.UploadResultDto;
import com.bluebug.gamehub.post_domain.domain.Post;
import com.bluebug.gamehub.post_domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {
    private final PostRepository postRepository;
    private final FileExtensionUtils fileExtensionUtils;
    private final AmazonS3 amazonS3;

    public ResponseEntity uploadPost(String nickname, UploadDto uploadDto, List<MultipartFile> multipartFiles) {
        if(multipartFiles.size() > 5){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                            .errors(List.of(new FieldErrorDto("files","파일 갯수가 5개 초과입니다.")))
                            .build());
        }
        List<String> filePaths = new ArrayList<>();
        String thumbnail = "";

        int idx = 0;
        for(MultipartFile multipartFile : multipartFiles){
            String originalFilename = multipartFile.getOriginalFilename();

            if(originalFilename == null || originalFilename.isBlank()){
                return ResponseEntity.badRequest().body(ErrorDto.builder()
                                .errors(List.of(new FieldErrorDto("files["+idx+"]","파일 이름이 없습니다.")))
                                .build());
            }

            String fileName = buildFileName(originalFilename);
            String folderName = fileName.substring(0, fileName.indexOf(fileExtensionUtils.getCATEGORY_SEPARATOR()));
            String bucketName = fileExtensionUtils.getBUCKET()+ "/"+ folderName;

            try (InputStream inputStream = multipartFile.getInputStream()) {
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(multipartFile.getContentType());
                objectMetadata.setContentLength(multipartFile.getBytes().length);

                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                if(idx == 0) thumbnail = folderName+"/"+fileName;
                else filePaths.add(folderName+"/"+fileName);
            }
            catch (AmazonServiceException e){
                log.error("uploadToAWS AmazonServiceException filePath={}, yyyymm={}, error={}", e.getMessage());
            }
            catch (SdkClientException e){
                log.error("uploadToAWS SdkClientException filePath={}, error={}", e.getMessage());
            }
            catch (IOException e) {
                return ResponseEntity.badRequest().body(ErrorDto.builder()
                                .errors(List.of(new FieldErrorDto("files","파일 쓰기 중 오류가 발생했습니다.")))
                                .build());
            }
            idx += 1;
        }

        Post newPost = postRepository.save(Post.createPost(nickname, uploadDto.getDescription(),
                thumbnail, uploadDto.getTags(), filePaths));

        UploadResultDto resultDto = UploadResultDto.builder()
                .status("upload success")
                .createdUrl(URI.create("/p/" + newPost.getId()))
                .filePaths(filePaths)
                .build();

        return ResponseEntity.created(resultDto.getCreatedUrl()).body(resultDto);
    }

    public ResponseEntity deletePost(String nickname, String postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                            .errors(List.of(new FieldErrorDto("Post Id"
                                    ,"해당 Post가 존재하지 않습니다.")))
                            .build());
        }
        if(!post.get().getNickname().equals(nickname)){
            return ResponseEntity.badRequest().body(ErrorDto.builder()
                    .errors(List.of(new FieldErrorDto("Post Id"
                            ,"해당 Post 작성자가 아닙니다.")))
                    .build());
        }

        List<String> filePaths = post.get().getFilePaths();
        for(String filePath : filePaths){
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(fileExtensionUtils.getBUCKET(), filePath));
            } catch (AmazonServiceException e){
                log.error("uploadToAWS AmazonServiceException filePath={}, yyyymm={}, error={}", e.getMessage());
            }
            catch (SdkClientException e){
                log.error("uploadToAWS SdkClientException filePath={}, error={}", e.getMessage());
            }
        }

        postRepository.deleteById(postId);
        return ResponseEntity.ok().body(DeleteResultDto.builder().status("delete success").message("post 삭제 성공").build());
    }

    private String buildFileName(String originalFileName) {
        String category = "etc";
        int fileExtensionIndex = originalFileName.lastIndexOf(fileExtensionUtils.getFILE_EXTENSION_SEPARATOR());
        String fileExtension = originalFileName.substring(fileExtensionIndex);

        if(fileExtensionUtils.getImage_extension().contains(fileExtension)){
            category = "img";
        }
        else if(fileExtensionUtils.getVideo_extension().contains(fileExtension)){
            category = "video";
        }

        return category + fileExtensionUtils.getCATEGORY_SEPARATOR() + LocalDateTime.now() + fileExtensionUtils.getUUID_SEPARATOR() + UUID.randomUUID() + fileExtension;
    }
}
