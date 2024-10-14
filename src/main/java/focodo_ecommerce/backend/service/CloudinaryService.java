package focodo_ecommerce.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public byte[] compressImage(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);  // Chuyển ảnh sang định dạng JPG nén
        return byteArrayOutputStream.toByteArray();
    }

    public String uploadOneFile(MultipartFile file, String folderName) {
        Map result = null;
        try {
            result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName,
                    "resource_type", "image"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.get("secure_url").toString();
    }
    public List<String> uploadMultipleFiles(List<MultipartFile> files, String folderName) {
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName,
                                "resource_type", "image"));
                        return result.get("secure_url").toString();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public void deleteMultipleFiles(List<String> files, String folderName) {
        List<String> nameImages = getNameImages(files, folderName);
        List<CompletableFuture<Void>> deletionFutures = nameImages.stream()
                .map(name -> CompletableFuture.runAsync(() -> {
                    try {
                        cloudinary.api().deleteResources(Arrays.asList(name),
                                ObjectUtils.asMap("type", "upload", "resource_type", "image"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .toList();

        CompletableFuture<Void> allDeletions = CompletableFuture.allOf(
                deletionFutures.toArray(new CompletableFuture[0]));

        try {
            allDeletions.get(); // Wait for all deletions to complete
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getNameImages(List<String> files, String folderName) {
        List<String> nameImages = new ArrayList<String>();
        for(String file: files) {
            nameImages.add(getNameOneImage(file, folderName));
        }
        return nameImages;
    }
    public String getNameOneImage(String file, String folderName) {
        int lastSlashIndex = file.lastIndexOf("/");

        // Cắt chuỗi từ vị trí sau dấu "/" cho đến hết chuỗi
        String fileNameWithExtension = file.substring(lastSlashIndex + 1);

        // Tách phần tên file và phần mở rộng
        String[] parts = fileNameWithExtension.split("\\.");
        return folderName + "/" + parts[0];
    }
}
